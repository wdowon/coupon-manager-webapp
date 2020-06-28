package com.jaeholee.coupon.coupon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * CouponData 클래스 및 테이블 형식.
 * Author : wdowon@gmail.com
 */
@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "IX_USER_ID", columnList = "USERID"),                 //UserId 대상 인덱스 추가
        @Index(name = "IX_USE_EXPIREDATE", columnList = "USE,EXPIREDATE")   //Use, ExpireDate 대상 인덱스 추가
})
public class CouponData {
    @Id
    @NotNull
    @Column(nullable = false, unique = true)
    private String couponCode;

    private String userId;

    @JsonIgnore
    private int expireDayCount;

    private boolean use = false;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime regDate;

    @JsonIgnore
    private LocalDateTime assignDate;

    private LocalDateTime expireDate;
}
