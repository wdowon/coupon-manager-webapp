package com.jaeholee.coupon.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Jwt member.
 * 로그인 후 데이터 응답 객체
 * Author : wdowon@gmail.com
 */
@Getter
@Setter
@AllArgsConstructor
public class JwtMember {
    private String token;
    private Member user;
}
