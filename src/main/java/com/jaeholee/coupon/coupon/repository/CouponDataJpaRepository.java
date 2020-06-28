package com.jaeholee.coupon.coupon.repository;

import com.jaeholee.coupon.coupon.domain.CouponData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The interface Coupon data jpa repository.
 * Author : wdowon@gmail.com
 */
public interface CouponDataJpaRepository extends JpaRepository<CouponData, String> {
    /**
     * Find by user id optional.
     *
     * @param userId the user id
     * @return the optional
     */
    Optional<List<CouponData>> findByUserId(String userId);

    /**
     * Find by user id and coupon code optional.
     *
     * @param userId     the user id
     * @param couponCode the coupon code
     * @return the optional
     */
    Optional<CouponData> findByUserIdAndCouponCode(String userId, String couponCode);

    /**
     * Find assign able coupon optional.
     *
     * @param userId the user id
     * @return the optional
     */
    @Query(value = "SELECT TOP 1 * FROM COUPONDATA WHERE USERID IS NULL", nativeQuery = true)
    Optional<CouponData> findAssignAbleCoupon(String userId);

    /**
     * Find expired coupon optional.
     *
     * @param currentDateTime the current date time
     * @return the optional
     */
    @Query(value = "SELECT * FROM COUPONDATA WHERE USE = 'N' AND EXPIREDATE < ?1", nativeQuery = true)
    Optional<List<CouponData>> findExpiredCoupon(LocalDateTime currentDateTime);

    /**
     * Find expired coupon for notice optional.
     *
     * @param currentDate the current date
     * @return the optional
     */
    @Query(value = "SELECT * FROM COUPONDATA WHERE USE = 'N' AND CAST(EXPIREDATE AS DATE) = ?1", nativeQuery = true)
    Optional<List<CouponData>> findExpiredCouponForNotice(LocalDate currentDate);
}
