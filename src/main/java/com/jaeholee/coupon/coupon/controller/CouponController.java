package com.jaeholee.coupon.coupon.controller;

import com.jaeholee.coupon.common.error.CommonResultCode;
import com.jaeholee.coupon.common.error.CommonResultResponse;
import com.jaeholee.coupon.common.exception.CommonException;
import com.jaeholee.coupon.coupon.domain.CouponData;
import com.jaeholee.coupon.coupon.domain.CouponReq;
import com.jaeholee.coupon.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Coupon controller.
 * 쿠폰 관련 기능 제공.
 * Author : wdowon@gmail.com
 */
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<CouponData> getCouponList(Principal principal) {
        return couponService.getCouponList(principal.getName());
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public CommonResultResponse createCoupon(@Valid CouponReq couponReq, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CommonException(CommonResultCode.INVALID_PARAMETER);
        }
        couponService.createCoupon(couponReq);
        return new CommonResultResponse(CommonResultCode.SUCCESS);
    }

    @RequestMapping(value = {"/assign"}, method = RequestMethod.POST)
    public CouponData assignCoupon(Principal principal) {
        return couponService.assignCoupon(principal.getName());
    }

    @RequestMapping(value = {"/apply"}, method = RequestMethod.POST)
    public CommonResultResponse applyCoupon(String couponCode, Principal principal) {
        if (StringUtils.isEmpty(couponCode)) {
            throw new CommonException(CommonResultCode.INVALID_PARAMETER);
        }

        return couponService.applyCoupon(principal.getName(), couponCode);
    }

    @RequestMapping(value = {"/cancel"}, method = RequestMethod.POST)
    public CommonResultResponse cancelCoupon(String couponCode, Principal principal) {
        if (StringUtils.isEmpty(couponCode)) {
            throw new CommonException(CommonResultCode.INVALID_PARAMETER);
        }

        return couponService.cancelCoupon(principal.getName(), couponCode);
    }

    @RequestMapping(value = {"/expired"}, method = RequestMethod.GET)
    public List<CouponData> getExpiredCouponList() {
        return couponService.getExpiredCouponList();
    }
}
