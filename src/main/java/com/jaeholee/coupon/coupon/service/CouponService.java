package com.jaeholee.coupon.coupon.service;

import com.jaeholee.coupon.common.error.CommonResultCode;
import com.jaeholee.coupon.common.error.CommonResultResponse;
import com.jaeholee.coupon.common.exception.CommonException;
import com.jaeholee.coupon.coupon.domain.CouponData;
import com.jaeholee.coupon.coupon.domain.CouponReq;
import com.jaeholee.coupon.coupon.enums.CouponStatus;
import com.jaeholee.coupon.coupon.repository.CouponDataJpaRepository;
import com.jaeholee.coupon.coupon.util.CouponGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * The type Coupon service.
 * Author : wdowon@gmail.com
 */
@Service
public class CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    /**
     * The Coupon data jpa repository.
     */
    private final CouponDataJpaRepository couponDataJpaRepository;

    /**
     * The Coupon log service.
     */
    private final CouponLogService couponLogService;

    @Autowired
    public CouponService(CouponDataJpaRepository couponDataJpaRepository, CouponLogService couponLogService) {
        this.couponDataJpaRepository = couponDataJpaRepository;
        this.couponLogService = couponLogService;
    }

    /**
     * Create coupon common result response.
     *
     * @param couponReq the coupon req
     * @return the common result response
     */
    @Async
    @Transactional(transactionManager = "couponDBTransactionManager")
    public void createCoupon(CouponReq couponReq) {

        logger.info("[CouponService] create coupon start : {} {}", couponReq.getCouponCount(), LocalDateTime.now());
        int createCount = 0;
        HashMap<String, Integer> createCouponMap = new HashMap<>();

        while (createCount < couponReq.getCouponCount()) {
            String couponCode = CouponGenerator.generateCoupon(couponReq.getCouponLength());

            if (createCouponMap.containsKey(couponCode)) {
                continue;
            }

            createCouponMap.put(couponCode, 1);

            CouponData couponData = new CouponData();
            couponData.setCouponCode(couponCode);
            couponData.setExpireDayCount(couponReq.getExpireDayCount());

            try {
                couponDataJpaRepository.save(couponData);
            } catch (DuplicateKeyException e) {
                //중복 쿠폰코드 삽입 exception 발생 시 다시 loop 수행
                continue;
            }

            createCount++;
        }

        logger.info("[CouponService] create coupon end : {} {}", couponReq.getCouponCount(), LocalDateTime.now());
    }

    /**
     * Assign coupon coupon data.
     *
     * @param userId the user id
     * @return the coupon data
     */
    @Transactional(transactionManager = "couponDBTransactionManager")
    public CouponData assignCoupon(String userId) {

        Optional<CouponData> assignAbleCoupon = couponDataJpaRepository.findAssignAbleCoupon(userId);

        CouponData couponData = assignAbleCoupon.orElseThrow(()
                -> new CommonException(CommonResultCode.ZERO_COUPON_QUANTITY));
        couponData.setUserId(userId);

        LocalDateTime assignDate = LocalDateTime.now();
        LocalDateTime expireDate = assignDate.plusDays(couponData.getExpireDayCount());

        couponData.setAssignDate(assignDate);
        couponData.setExpireDate(expireDate);

        couponLogService.saveCouponLog(couponData.getCouponCode(), CouponStatus.ASSIGN);

        return couponData;
    }

    /**
     * Gets coupon list.
     *
     * @param userId the user id
     * @return the coupon list
     */
    public List<CouponData> getCouponList(String userId) {
        Optional<List<CouponData>> couponDataList = couponDataJpaRepository.findByUserId(userId);

        couponDataList.orElseThrow(()
                -> new CommonException(CommonResultCode.NO_DATA));

        return couponDataList.get();
    }

    /**
     * Apply coupon common result response.
     *
     * @param userId     the user id
     * @param couponCode the coupon code
     * @return the common result response
     */
    @Transactional(transactionManager = "couponDBTransactionManager")
    public CommonResultResponse applyCoupon(String userId, String couponCode) {
        LocalDateTime currentDate = LocalDateTime.now();

        //쿠폰 조회(사용여부, 할당여부)
        Optional<CouponData> applyTargetCoupon = couponDataJpaRepository.findByUserIdAndCouponCode(userId, couponCode);

        CouponData couponData = applyTargetCoupon.orElseThrow(()
                -> new CommonException(CommonResultCode.NO_DATA));

        //사용 처리 된 쿠폰의 경우 에러
        if (couponData.isUse()) {
            throw new CommonException(CommonResultCode.ALREADY_USE_COUPON);
        }

        //만료된 쿠폰의 경우 에러
        if (currentDate.isAfter(couponData.getExpireDate())) {
            throw new CommonException(CommonResultCode.COUPON_APPLY_EXPIRE);
        }

        //쿠폰 사용처리
        couponData.setUse(true);

        //로그 저장
        couponLogService.saveCouponLog(couponCode, CouponStatus.APPLY);

        return new CommonResultResponse(CommonResultCode.SUCCESS);
    }

    /**
     * Cancel coupon common result response.
     *
     * @param userId     the user id
     * @param couponCode the coupon code
     * @return the common result response
     */
    @Transactional(transactionManager = "couponDBTransactionManager")
    public CommonResultResponse cancelCoupon(String userId, String couponCode) {
        //쿠폰 조회(사용여부, 할당여부)
        Optional<CouponData> cancelTargetCoupon = couponDataJpaRepository.findByUserIdAndCouponCode(userId, couponCode);

        CouponData couponData = cancelTargetCoupon.orElseThrow(()
                -> new CommonException(CommonResultCode.NO_DATA));

        //미사용된 쿠폰의 경우 에러
        if (!couponData.isUse()) {
            throw new CommonException(CommonResultCode.NOT_USE_COUPON);
        }

        //취소의 경우는 만료 여부 체크하지 않음

        //쿠폰 미사용처리
        couponData.setUse(false);

        //로그 저장
        couponLogService.saveCouponLog(couponCode, CouponStatus.CANCEL);

        return new CommonResultResponse(CommonResultCode.SUCCESS);
    }

    /**
     * Gets expired coupon list.
     *
     * @return the expired coupon list
     */
    public List<CouponData> getExpiredCouponList() {
        LocalDateTime currentDate = LocalDateTime.now();

        Optional<List<CouponData>> couponDataList = couponDataJpaRepository.findExpiredCoupon(currentDate);

        couponDataList.orElseThrow(()
                -> new CommonException(CommonResultCode.NO_DATA));

        return couponDataList.get();
    }

}
