package com.jaeholee.coupon.common.error;

/**
 * Common result code.
 * 공통 응답 코드
 * Author : wdowon@gmail.com
 */
public enum CommonResultCode {

    SUCCESS(0, "Success"),
    NO_DATA(1, "No data"),
    INTERNAL_SERVER_ERROR(2, "Internal server error"),
    INVALID_PARAMETER(3, "Invalid parameter"),
    SIGNIN_FAILURE(4, "Sign in failed"),
    ALREADY_EXIST_USERID(5, "Already exist userId"),
    USER_NOT_FOUND(6, "User not found"),
    ZERO_COUPON_QUANTITY(7, "Coupon quantity is zero"),
    ALREADY_USE_COUPON(8, "Coupon already used"),
    NOT_USE_COUPON(9, "Coupon not used"),
    COUPON_APPLY_EXPIRE(10, "Coupon apply expired");

    private Integer errorCode;
    private String errorMsg;

    CommonResultCode(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
