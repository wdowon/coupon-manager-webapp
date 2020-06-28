package com.jaeholee.coupon.coupon.service;

import com.jaeholee.coupon.coupon.domain.CouponLog;
import com.jaeholee.coupon.coupon.enums.CouponStatus;
import com.jaeholee.coupon.coupon.repository.CouponLogJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Coupon log service.
 * Author : wdowon@gmail.com
 */
@Service
public class CouponLogService {

    private static final Logger logger = LoggerFactory.getLogger(CouponLogService.class);

    /**
     * The Coupon log jpa repository.
     */
    private final CouponLogJpaRepository couponLogJpaRepository;

    @Autowired
    public CouponLogService(CouponLogJpaRepository couponLogJpaRepository) {
        this.couponLogJpaRepository = couponLogJpaRepository;
    }

    /**
     * Save coupon log.
     *
     * @param couponCode   the coupon code
     * @param couponStatus the coupon status
     */
    public void saveCouponLog(String couponCode, CouponStatus couponStatus) {
        CouponLog couponLog = new CouponLog();
        couponLog.setCouponCode(couponCode);
        couponLog.setCouponStatus(couponStatus.getStatus());

        couponLogJpaRepository.save(couponLog);
    }
}
