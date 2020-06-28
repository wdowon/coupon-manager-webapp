package com.jaeholee.coupon.coupon.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * CouponLog 클래스 및 테이블 형식.
 * Author : wdowon@gmail.com
 */
@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "IX_COUPON_CODE", columnList = "COUPONCODE")      //couponCode 대상 인덱스 추가
})
public class CouponLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long seq;

    @NotNull
    private String couponCode;

    @NotNull
    private String couponStatus;

    @CreationTimestamp
    private LocalDateTime regDate;
}
