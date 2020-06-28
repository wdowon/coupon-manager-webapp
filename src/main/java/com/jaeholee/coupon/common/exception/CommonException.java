package com.jaeholee.coupon.common.exception;

import com.jaeholee.coupon.common.error.CommonResultCode;

/**
 * Common exception.
 * 공통 Exception
 * Author : wdowon@gmail.com
 */
public class CommonException extends RuntimeException {

    private Integer errorCode;
    private String errorMsg;

    public CommonException(CommonResultCode errorCode) {
        super(errorCode.getErrorMsg());
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = errorCode.getErrorMsg();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
