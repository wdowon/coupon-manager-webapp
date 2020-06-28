package com.jaeholee.coupon.coupon.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Coupon generator.
 * Author : wdowon@gmail.com
 */
public class CouponGenerator {

    private static final char[] CHARACTERS =
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * Generate coupon string.
     *
     * @param couponLength the coupon length
     * @return the string
     */
    public static String generateCoupon(int couponLength) {

        char[] couponCode = new char[couponLength];

        for (int i = 0; i < couponCode.length; i++) {

            // 문자 배열의 문자 결정
            int elementIndex = ThreadLocalRandom.current().nextInt(CHARACTERS.length);

            // 순차적으로 랜덤 문자 할당
            couponCode[i] = CHARACTERS[elementIndex];
        }

        return String.valueOf(couponCode);
    }
}
