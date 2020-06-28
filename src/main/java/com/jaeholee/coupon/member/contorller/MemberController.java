package com.jaeholee.coupon.member.contorller;

import com.jaeholee.coupon.common.error.CommonResultCode;
import com.jaeholee.coupon.common.error.CommonResultResponse;
import com.jaeholee.coupon.common.exception.CommonException;
import com.jaeholee.coupon.member.domain.JwtMember;
import com.jaeholee.coupon.member.domain.Member;
import com.jaeholee.coupon.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Member controller.
 * 회원가입 및 로그인 제공.
 * Author : wdowon@gmail.com
 */
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.POST)
    public CommonResultResponse createMember(@Valid Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CommonException(CommonResultCode.INVALID_PARAMETER);
        }

        return memberService.createMember(member);
    }

    @RequestMapping(value = {"/signin"}, method = RequestMethod.GET)
    public JwtMember signin(@Valid Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CommonException(CommonResultCode.INVALID_PARAMETER);
        }
        return memberService.loginMember(member);
    }
}
