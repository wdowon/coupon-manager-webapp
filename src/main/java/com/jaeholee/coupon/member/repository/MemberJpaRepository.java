package com.jaeholee.coupon.member.repository;

import com.jaeholee.coupon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Member jpa repository.
 * Author : wdowon@gmail.com
 */
public interface MemberJpaRepository extends JpaRepository<Member, String> {
    /**
     * Find by user id optional.
     *
     * @param userId the user id
     * @return the optional
     */
    Optional<Member> findByUserId(String userId);
}
