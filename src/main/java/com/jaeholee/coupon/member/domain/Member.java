package com.jaeholee.coupon.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Member 클래스 및 테이블 형식.
 * Author : wdowon@gmail.com
 */
@Entity
@Getter
@Setter
public class Member {

    @Id
    @NotNull
    @Column(nullable = false, unique = true)
    private String userId;

    @NotNull
    @JsonIgnore
    @Column(nullable = false)
    private String userPw;

    @CreationTimestamp
    private LocalDateTime regDate;
}
