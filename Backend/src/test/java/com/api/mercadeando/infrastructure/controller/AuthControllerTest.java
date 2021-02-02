package com.api.mercadeando.infrastructure.controller;

import com.api.mercadeando.infrastructure.persistence.entity.VerificationToken;
import com.api.mercadeando.infrastructure.persistence.jpa.UserJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.VerificationTokenJPARepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_AUTH_V1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author cristianmarint
 * @Date 2021-02-02 11:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private VerificationTokenJPARepository verificationTokenJPARepository;
    @Autowired
    private UserJPARepository userJPARepository;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    private static final String EMAIL = "testusercontroller@email.com";
    private static final String USERNAME = "testusercontroller";
    private static final String PASSWORD_PLAIN  = "123456789";

    @Test
    public void signUp_statusOkAndMessage_ifAllDataIsValid() throws Exception{
        String body = "{\"username\":\"" + USERNAME + "\", \"password\":\""+ PASSWORD_PLAIN + "\",\"email\":\""+EMAIL+"\"}";
        mvc.perform(post(URL_AUTH_V1+"signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string("User Registration Successful"))
                .andExpect(status().isOk());
    }

    @Test
    public void verifyAccount_statusOkAndMessage_ifTokenIsValid() throws Exception {
        Long testUserId = userJPARepository.findByUsername(USERNAME).get().getId();
        Optional<VerificationToken> token = verificationTokenJPARepository.findByUserId(testUserId);
        mvc.perform(get(URL_AUTH_V1+"account-verification/"+token.get().getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL))
                .andExpect(content().string("Account activated successfully"))
                .andExpect(status().isOk());
    }

    @Test
    public void login_statusOk_IfAccountIsValid() throws Exception{
        String body = "{\"username\":\"" + USERNAME + "\", \"password\":\""+ PASSWORD_PLAIN + "\"}";
        mvc.perform(post(URL_AUTH_V1+"login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }
}
