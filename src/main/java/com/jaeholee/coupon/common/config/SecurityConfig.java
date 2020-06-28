package com.jaeholee.coupon.common.config;

import com.jaeholee.coupon.common.handler.SigninFailureHandler;
import com.jaeholee.coupon.member.jwt.AuthProvider;
import com.jaeholee.coupon.member.jwt.JwtAuthenticationFilter;
import com.jaeholee.coupon.member.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security config.
 * Author : wdowon@gmail.com
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SigninFailureHandler signinFailureHandler;
    private final AuthProvider authProvider;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(AuthenticationManagerBuilder authenticationManagerBuilder, SigninFailureHandler signinFailureHandler, AuthProvider authProvider, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.signinFailureHandler = signinFailureHandler;
        this.authProvider = authProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //member 관련 API 및 DB console 인증 예외처리
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/api/member/**", "/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //h2 콘솔 접근 필요하여 해당 경로 및 header에 대해서만 예외처리
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests() // 다음 요청 대한 사용권한 체크
                .antMatchers("/api/member/**").permitAll()
                .antMatchers("/h2-console/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated()

                .and()
                .csrf()
                .disable()
                .headers().frameOptions().disable()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(signinFailureHandler)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(signinFailureHandler)

                .and()
                .authenticationProvider(authProvider)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
