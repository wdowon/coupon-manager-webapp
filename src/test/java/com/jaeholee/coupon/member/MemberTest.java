package com.jaeholee.coupon.member;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaeholee.coupon.common.error.CommonResultCode;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Member test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberTest {
    private final String SIGN_UP_URI = "/api/member/signup";
    private final String SIGN_IN_URI = "/api/member/signin";
    private final String id = "memberTestId";
    private final String password = "memberTestPw";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    /**
     * 회원가입 테스트
     */
    @Test
    public void A_signUp_success() throws Exception {

        MvcResult result = mockMvc.perform(post(SIGN_UP_URI)
                .param("userId", id)
                .param("userPw", password))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SUCCESS.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 회원가입 테스트 (이미 존재하는 계정, A 테스트가 선행되어야 함)
     */
    @Test
    public void B_signUp_fail_already_exist() throws Exception {

        MvcResult result = mockMvc.perform(post(SIGN_UP_URI)
                .param("userId", id)
                .param("userPw", password))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.ALREADY_EXIST_USERID.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }


    /**
     * 로그인 테스트
     */
    @Test
    public void C_signIn() throws Exception {

        MvcResult result = mockMvc.perform(get(SIGN_IN_URI)
                .param("userId", id)
                .param("userPw", password))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertNotNull(response.get("token"));
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 로그인 테스트(잘못된 비밀번호)
     */
    @Test
    public void D_signIn_fail_invaliPassword() throws Exception {
        String wrongPassword = "wrong_password";

        MvcResult result = mockMvc.perform(get(SIGN_IN_URI)
                .param("userId", id)
                .param("userPw", wrongPassword))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.SIGNIN_FAILURE.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }

    /**
     * 로그인 테스트(잘못된 비밀번호)
     */
    @Test
    public void F_signIn_fail_invaliId() throws Exception {
        String wrongId = "wrong_id";

        MvcResult result = mockMvc.perform(get(SIGN_IN_URI)
                .param("userId", wrongId)
                .param("userPw", password))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.get("errorCode").asInt() == CommonResultCode.USER_NOT_FOUND.getErrorCode());
        assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
    }
}
