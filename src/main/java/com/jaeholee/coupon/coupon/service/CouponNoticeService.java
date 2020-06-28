package com.jaeholee.coupon.coupon.service;

import com.jaeholee.coupon.coupon.constants.CouponConstants;
import com.jaeholee.coupon.coupon.domain.CouponData;
import com.jaeholee.coupon.coupon.repository.CouponDataJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The type Coupon notice service.
 * Author : wdowon@gmail.com
 */
@Service
public class CouponNoticeService {

    private static final Logger logger = LoggerFactory.getLogger(CouponNoticeService.class);

    /**
     * The Coupon data jpa repository.
     */
    private final CouponDataJpaRepository couponDataJpaRepository;

    @Autowired
    public CouponNoticeService(CouponDataJpaRepository couponDataJpaRepository) {
        this.couponDataJpaRepository = couponDataJpaRepository;
    }

    /**
     * Coupon expire notice.
     * 현재 확인을 위해 10초에 한번씩 스케쥴링 하도록 설정.
     */
    @Scheduled(cron = "*/10 * * * * *")
    public void couponExpireNotice() {
        LocalDate noticeDate = LocalDate.now().plusDays(CouponConstants.DEFAULT_NOTICE_REMAIN_EXPIRE_DAY_COUNT);

        Optional<List<CouponData>> couponDataList = couponDataJpaRepository.findExpiredCouponForNotice(noticeDate);

        System.out.println("[" + LocalDate.now() + "]");
        if (!couponDataList.isPresent()) {
            System.out.println("스케쥴링 대상 없음");
        } else {
            for (CouponData couponData : couponDataList.get()) {
                System.out.println("사용자 : " + couponData.getUserId() + "님의 쿠폰 "
                        + couponData.getCouponCode() + "이 3일 뒤 만료됩니다.");
            }
        }
    }

}
