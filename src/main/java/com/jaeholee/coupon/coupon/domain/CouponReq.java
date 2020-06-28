package com.jaeholee.coupon.coupon.domain;

import com.jaeholee.coupon.coupon.constants.CouponConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * CouponReq 요청 클래스.
 * Author : wdowon@gmail.com
 */
@Getter
@Setter
public class CouponReq {
    @Min(1)
    private int couponCount;
    private int couponLength = CouponConstants.DEFAULT_COUPON_LENGTH;
    private int expireDayCount = CouponConstants.DEFAULT_EXPIRE_DAY_COUNT;
}
