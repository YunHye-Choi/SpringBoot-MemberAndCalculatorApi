package com.ai21.Assignment1.integration.service;

import com.ai21.Assignment1.repository.UserRepository;
import com.ai21.Assignment1.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

//todo - 잘 모르겠음 (제대로 테스팅 된 거 아닌듯)
@SpringBootTest
@Transactional
class CustomUserDetailsServiceTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "user1", roles = {"USER", "ADMIN"})
    void loadUserByUsernameTest(){
        UserDetails userDetailsOfUser = customUserDetailsService.loadUserByUsername("user1");

        String expected = "user1";
        String result = userDetailsOfUser.getUsername();

        assertEquals(expected, result);
    }
}
