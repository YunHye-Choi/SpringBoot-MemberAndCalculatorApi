package com.ai21.Assignment1.integration.service;

import com.ai21.Assignment1.dto.request.CalculatorRequestDto;
import com.ai21.Assignment1.dto.service.CalculatorDto;
import com.ai21.Assignment1.exception.InvalidFormulaException;
import com.ai21.Assignment1.repository.CalculatorRepository;
import com.ai21.Assignment1.repository.UserRepository;
import com.ai21.Assignment1.service.CalculatorService;
import com.ai21.Assignment1.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
//todo 전반적으로 research 문서함의 통합테스트 자료에 맞게 리팩터링이 필요
// - 특히 service 단의 메서드가 아니라 실제로 mvc를 통해 get/post를 controller로 쏴주는 방식을 적용하는 게 중요해보임
@SpringBootTest
@Transactional
public class CalculatorServiceTest {
    @Autowired
    private CalculatorService calculatorService;

    @Autowired
    private CalculatorRepository calculatorRepository;

    @Autowired
    private UserRepository userRepository;

    private void setUserToContextByUsername(String username) {
        CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

    @Test
    void 계산후기록저장 () {
        //given
        setUserToContextByUsername("user1");
        CalculatorRequestDto requestDto = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=3").build();

        //when
        calculatorService.calculateAndSaveHistory("user1", requestDto);
        Pageable pageable = PageRequest.of(0,5);
        Double result = calculatorRepository.findByUsername(pageable, "user1").getContent().get(0).getCorrectAnswer();

        //then
        Double expected = 7.0;
        assertThat(result.equals(expected));
    }

    @Test
    void 계산후기록저장실패_없는회원 () {
        //given
        UsernameNotFoundException e = assertThrows(UsernameNotFoundException.class, () -> setUserToContextByUsername("fff"));

        //then
        assertThat(e.getMessage()).isEqualTo("fff -> 데이터베이스에서 찾을 수 없습니다.");
    }

    @Test
    void 계산후기록저장실패_invalidFormula () {
        //given
        setUserToContextByUsername("user1");
        CalculatorRequestDto requestDto = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=").build();

        //when
        InvalidFormulaException e = assertThrows(InvalidFormulaException.class, () ->  calculatorService.calculateAndSaveHistory("user1", requestDto));
        //then
        assertThat(e.getMessage()).isEqualTo("Invalid formula: 1+3+(-6)/(-2)=");
    }

    @Test
    void 연산결과조회 () {
        //given
        setUserToContextByUsername("user1");

        CalculatorRequestDto requestDto1 = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=3").build();
        calculatorService.calculateAndSaveHistory("user1", requestDto1);

        CalculatorRequestDto requestDto2 = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=7").build();
        calculatorService.calculateAndSaveHistory("user1", requestDto2);

        CalculatorRequestDto requestDto3 = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=3").build();
        calculatorService.calculateAndSaveHistory("user1", requestDto3);

        CalculatorRequestDto requestDto4 = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=7").build();
        calculatorService.calculateAndSaveHistory("user1", requestDto4);

        CalculatorRequestDto requestDto5 = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=3").build();
        calculatorService.calculateAndSaveHistory("user1", requestDto5);

        CalculatorRequestDto requestDto6 = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=7").build();
        calculatorService.calculateAndSaveHistory("user1", requestDto6);

        Pageable pageable = PageRequest.of(1,5);

        Page<CalculatorDto> result = calculatorService.inquireCalculatorHistory("user1", pageable);

        //when
        assertThat(result.getTotalElements() == 6);
        assertThat(result.getTotalPages() == 2);
    }

    @Test
    void 연산결과조회실패_없는회원 () {
        //given
        UsernameNotFoundException e = assertThrows(UsernameNotFoundException.class, () -> setUserToContextByUsername("fff"));

        //then
        assertThat(e.getMessage()).isEqualTo("fff -> 데이터베이스에서 찾을 수 없습니다.");
    }
}
