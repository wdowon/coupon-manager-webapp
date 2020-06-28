package com.jaeholee.coupon.common.error;

import com.jaeholee.coupon.common.exception.CommonException;

/**
 * Common result response.
 * 공통 리턴 객체
 * Author : wdowon@gmail.com
 */
public class CommonResultResponse {

    private Integer errorCode;
    private String errorMsg;

    public CommonResultResponse(CommonException commonException) {
        this.errorCode = commonException.getErrorCode();
        this.errorMsg = commonException.getErrorMsg();
    }

    public CommonResultResponse(Exception exception) {
        this.errorCode = 99999;
        this.errorMsg = exception.toString();
    }

    public CommonResultResponse(CommonResultCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = errorCode.getErrorMsg();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void serResponse(CommonResultCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = errorCode.getErrorMsg();
    }
}
