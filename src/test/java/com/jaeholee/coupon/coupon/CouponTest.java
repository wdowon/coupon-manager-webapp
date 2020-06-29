package com.jaeholee.coupon.coupon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaeholee.coupon.common.error.CommonResultCode;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;

import java.util.Iterator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Coupon test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CouponTest {

    private final String SIGN_UP_URI = "/api/member/signup";
    private final String SIGN_IN_URI = "/api/member/signin";
    private final String id = "couponTestId";
    private final String password = "couponTestPw";

    private final String CREATE_COUPON_URI = "/api/coupon/create";
    private final String ASSIGN_COUPON_URI = "/api/coupon/assign";
    private final String MY_COUPON_LIST_URI = "/api/coupon";
    private final String APPLY_COUPON_URI = "/api/coupon/apply";
    private final String CANCEL_COUPON_URI = "/api/coupon/cancel";
    private final String EXPIRED_COUPON_LIST_URI = "/api/coupon/expired";

    static String token;

    static String[] couponCode = new String[3];

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    /**
     * 토큰 획득(선행되어야 함)
     */
    @Before
    public void get_token() throws Exception {
        if(StringUtils.isEmpty(token)) {
            MvcResult result = mockMvc.perform(post(SIGN_UP_URI)
                    .param("userId", id)
                    .param("userPw", password))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();

            result = mockMvc.perform(get(SIGN_IN_URI)
                    .param("userId", id)
                    .param("userPw", password))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
            assertNotNull(response.get("token").toString());
            assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());

            token = response.get("token").asText();
        }
    }

    /**
     * 쿠폰 생성 테스트
     */
    @Test
    public void A_coupon_create() throws Exception {

        for(int i  = 0 ; i < 2 ; i ++) {
            //쿠폰 수량은 2개 1개로 나눠서 총 3개 생성, 2개는 만료일 3일, 1개는 만료일 도래 테스트용
            String couponCount = (i == 0 ? "2" : "1");
            String expireDayCount = (i == 0 ? "3" : "-1");
            MvcResult result = mockMvc.perform(post(CREATE_COUPON_URI)
                    .header("Authorization", "Bearer " + token)
                    .param("couponCount", couponCount)
                    .param("couponLength", "20")
                    .param("expireDayCount", expireDayCount))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
            assertTrue(response.get("errorCode").asInt() == CommonResultCode.SUCCESS.getErrorCode());
            assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
        }
    }


    /**
     * 쿠폰 생성 테스트 실패(필수 파라미터인 쿠폰 수량 입력 하지 않음)
     */
    @Test
    public void B_coupon_create_fail() throws Exception {

        MvcResult result = mockMvc.perform(post(CREATE_COUPON_URI)
                .header("Authorization", "Bearer " + token)
                .param("couponLength", "20")
                .param("expireDayCount", "3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.INVALID_PARAMETER.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 쿠폰 할당 테스트(발급 수량만큼 할당)
     */
    @Test
    public void C_coupon_assign() throws Exception {

        for(int i = 0 ; i < 3 ; i++) {
            MvcResult result = mockMvc.perform(post(ASSIGN_COUPON_URI)
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
            assertNotNull(response.get("couponCode").toString());
            assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
        }
    }

    /**
     * 쿠폰 할당 테스트 실패(쿠폰 없을경우)
     */
    @Test
    public void D_coupon_assign_fail() throws Exception {

        MvcResult result = mockMvc.perform(post(ASSIGN_COUPON_URI)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.ZERO_COUPON_QUANTITY.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 내 쿠폰 조회
     */
    @Test
    public void E_my_coupon_list() throws Exception {
        MvcResult result = mockMvc.perform(get(MY_COUPON_LIST_URI)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());

        int i = 0;
        for (Iterator<JsonNode> iterator = response.elements(); iterator.hasNext(); i++) {
            JsonNode children = objectMapper.readTree(iterator.next().toString());
            assertNotNull(children.get("couponCode").toString());

            couponCode[i] = children.get("couponCode").asText();

        }
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 쿠폰 사용 테스트
     */
    @Test
    public void F_coupon_apply() throws Exception {
        MvcResult result = mockMvc.perform(post(APPLY_COUPON_URI)
                .header("Authorization", "Bearer " + token)
                .param("couponCode", couponCode[1]))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SUCCESS.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 쿠폰 사용 테스트 실패(이미 사용한 쿠폰)
     */
    @Test
    public void G_coupon_apply_fail_already_use() throws Exception {
        MvcResult result = mockMvc.perform(post(APPLY_COUPON_URI)
                .header("Authorization", "Bearer " + token)
                .param("couponCode", couponCode[1]))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.ALREADY_USE_COUPON.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 쿠폰 사용 테스트 실패(잘못된 쿠폰번호)
     */
    @Test
    public void H_coupon_apply_fail_invalid_coupon() throws Exception {
        String wrongCoupon = "wrongCoupon";
        MvcResult result = mockMvc.perform(post(APPLY_COUPON_URI)
                .header("Authorization", "Bearer " + token)
                .param("couponCode", wrongCoupon))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.NO_DATA.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 쿠폰 취소 테스트
     */
    @Test
    public void I_coupon_cancel() throws Exception {
        MvcResult result = mockMvc.perform(post(CANCEL_COUPON_URI)
                .header("Authorization", "Bearer " + token)
                .param("couponCode", couponCode[1]))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SUCCESS.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 쿠폰 취소 테스트 실패(아직 사용되지 않은 쿠폰)
     */
    @Test
    public void J_coupon_apply_fail_already_use() throws Exception {
        MvcResult result = mockMvc.perform(post(CANCEL_COUPON_URI)
                .header("Authorization", "Bearer " + token)
                .param("couponCode", couponCode[1]))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.NOT_USE_COUPON.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 쿠폰 만료 리스트
     */
    @Test
    public void K_coupon_expired_list() throws Exception {
        MvcResult result = mockMvc.perform(get(EXPIRED_COUPON_LIST_URI)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());

        int i = 0;
        for (Iterator<JsonNode> iterator = response.elements(); iterator.hasNext(); i++) {
            JsonNode children = objectMapper.readTree(iterator.next().toString());
            assertNotNull(children.get("couponCode").toString());

        }
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 모든 쿠폰 API에 대해 잘못된 토큰 응답 테스트
     */
    @Test
    public void L_fail_invalidToken() throws Exception {
        String wrongToken = "wrong_token";
        MvcResult result = mockMvc.perform(post(CREATE_COUPON_URI)
                .header("Authorization", "Bearer " + wrongToken))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SIGNIN_FAILURE.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value());

        result = mockMvc.perform(post(ASSIGN_COUPON_URI)
                .header("Authorization", "Bearer " + wrongToken))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();

        response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SIGNIN_FAILURE.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value());

        result = mockMvc.perform(get(MY_COUPON_LIST_URI)
                .header("Authorization", "Bearer " + wrongToken))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();

        response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SIGNIN_FAILURE.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value());

        result = mockMvc.perform(post(APPLY_COUPON_URI)
                .header("Authorization", "Bearer " + wrongToken))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();

        response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SIGNIN_FAILURE.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value());

        result = mockMvc.perform(post(CANCEL_COUPON_URI)
                .header("Authorization", "Bearer " + wrongToken))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();

        response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SIGNIN_FAILURE.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value());

        result = mockMvc.perform(get(EXPIRED_COUPON_LIST_URI)
                .header("Authorization", "Bearer " + wrongToken))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();

        response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SIGNIN_FAILURE.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value());
    }

}
