package com.ai21.Assignment1.unit.service;

import com.ai21.Assignment1.domain.Authority;
import com.ai21.Assignment1.domain.User;
import com.ai21.Assignment1.repository.UserRepository;
import com.ai21.Assignment1.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameTest(){
        //given
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        when(userRepository.findOneWithAuthoritiesByUsername("user1"))
                .thenReturn(
                        Optional.ofNullable(User.builder()
                                        .username("user1")
                                        .password("1234")
                                        .authorities(Collections.singleton(authority))
                                        .build())
                );

        String expected = "user1";

        //when
        UserDetails userDetailsOfUser = customUserDetailsService.loadUserByUsername("user1");
        String result = userDetailsOfUser.getUsername();

        //then
        assertEquals(expected,result);
    }
}