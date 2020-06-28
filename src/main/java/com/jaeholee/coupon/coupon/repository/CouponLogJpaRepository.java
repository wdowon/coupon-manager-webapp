package com.jaeholee.coupon.coupon.repository;

import com.jaeholee.coupon.coupon.domain.CouponLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Coupon log jpa repository.
 * Author : wdowon@gmail.com
 */
public interface CouponLogJpaRepository extends JpaRepository<CouponLog, String> {

}
