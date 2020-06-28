package com.jaeholee.coupon.member.jwt;

import com.jaeholee.coupon.member.domain.JwtMember;
import com.jaeholee.coupon.member.domain.Member;
import com.jaeholee.coupon.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

/**
 * The type Auth provider.
 * Author : wdowon@gmail.com
 */
@Component
public class AuthProvider implements AuthenticationProvider {

    /**
     * The Member service.
     */
    private final MemberService memberService;

    /**
     * The Jwt token provider.
     */
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenCreater jwtTokenCreater;

    @Autowired
    public AuthProvider(MemberService memberService, JwtTokenProvider jwtTokenProvider, JwtTokenCreater jwtTokenCreater) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenCreater = jwtTokenCreater;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //DB에서 유저 정보 조회
        Member member = memberService.getMember(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
        if (member != null) {
            //AuthToken 생성 후 반환
            JwtAuthToken jwtAuthToken = new JwtAuthToken(member.getUserId(), member.getUserPw(), createAuthorityList("ROLE_USER"));
            jwtAuthToken.setDetails(new JwtMember(jwtTokenCreater.createToken(member.getUserId()), member));

            return jwtAuthToken;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(JwtAuthToken.class);
    }
}
