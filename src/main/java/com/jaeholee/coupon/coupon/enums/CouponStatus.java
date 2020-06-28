package com.jaeholee.coupon.coupon.enums;

import lombok.Getter;

/**
 * CouponStatus 쿠폰 상태 변경 저장 Type.
 * Author : wdowon@gmail.com
 */
@Getter
public enum CouponStatus {
    ASSIGN("assign"),
    APPLY("apply"),
    CANCEL("cancel");

    private String status;

    CouponStatus(String status) {
        this.status = status;
    }
}
