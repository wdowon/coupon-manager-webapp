package com.jaeholee.coupon.member.service;

import com.jaeholee.coupon.common.error.CommonResultCode;
import com.jaeholee.coupon.common.error.CommonResultResponse;
import com.jaeholee.coupon.common.exception.CommonException;
import com.jaeholee.coupon.member.domain.JwtMember;
import com.jaeholee.coupon.member.domain.Member;
import com.jaeholee.coupon.member.jwt.JwtTokenCreater;
import com.jaeholee.coupon.member.repository.MemberJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Member service.
 * 회원가입 및 로그인, 유저정보 조회
 * Author : wdowon@gmail.com
 */
@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenCreater jwtTokenCreater;

    @Autowired
    public MemberService(MemberJpaRepository memberJpaRepository, PasswordEncoder passwordEncoder, JwtTokenCreater jwtTokenCreater) {
        this.memberJpaRepository = memberJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenCreater = jwtTokenCreater;
    }

    @Transactional(transactionManager = "memberDBTransactionManager")
    public CommonResultResponse createMember(Member member) {
        //이미 존재하는 유저인지 검색
        Optional<Member> userInfo = memberJpaRepository.findByUserId(member.getUserId());

        if (userInfo.isPresent()) {
            throw new CommonException(CommonResultCode.ALREADY_EXIST_USERID);
        }

        try {
            //회원가입
            member.setUserId(member.getUserId());
            member.setUserPw(passwordEncoder.encode(member.getUserPw()));

            memberJpaRepository.save(member);
        } catch (Exception e) {
            throw new CommonException(CommonResultCode.INTERNAL_SERVER_ERROR);
        }

        return new CommonResultResponse(CommonResultCode.SUCCESS);
    }

    public Member getMember(String userId, String userPw) {
        Member member = getMember(userId);

        if (!passwordEncoder.matches(userPw, member.getUserPw())) {
            throw new CommonException(CommonResultCode.SIGNIN_FAILURE);
        }
        return member;
    }

    public Member getMember(String userId) {
        Optional<Member> userInfo = memberJpaRepository.findByUserId(userId);

        if (!userInfo.isPresent()) {
            throw new CommonException(CommonResultCode.USER_NOT_FOUND);
        }

        return userInfo.get();
    }

    public JwtMember loginMember(Member reqMember) {
        Member member = getMember(reqMember.getUserId());

        if (!passwordEncoder.matches(reqMember.getUserPw(), member.getUserPw())) {
            throw new CommonException(CommonResultCode.SIGNIN_FAILURE);
        }

        return new JwtMember(jwtTokenCreater.createToken(member.getUserId()), member);
    }
}
