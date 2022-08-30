package com.ai21.Assignment1.unit.service;

import com.ai21.Assignment1.domain.Calculator;
import com.ai21.Assignment1.domain.User;
import com.ai21.Assignment1.dto.request.CalculatorRequestDto;
import com.ai21.Assignment1.dto.service.CalculatorDto;
import com.ai21.Assignment1.exception.InvalidFormulaException;
import com.ai21.Assignment1.repository.CalculatorRepository;
import com.ai21.Assignment1.repository.UserRepository;
import com.ai21.Assignment1.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {
    @Mock
    private CalculatorRepository calculatorRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CalculatorService calculatorService;

    @BeforeEach
    void userSetting(){

        userRepository.findByUsername("user1");
    }

    @Test
    void 계산후기록저장 () {
        //given
        CalculatorRequestDto requestDto = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=3").build();

        //when
        when(userRepository.findByUsername("user1")).thenReturn(User.builder().username("user1").password("$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC").build());

        CalculatorDto calcResult = calculatorService.calculateAndSaveHistory("user1", requestDto);

        //then
        Double expected = 7.0;
        assertThat(calcResult.getCorrectAnswer().equals(expected));
    }

    @Test
    void 계산후기록저장실패_invalidFormula () {
        //given
        CalculatorRequestDto requestDto = CalculatorRequestDto.builder().formula("1+3+(-6)/(-2)=").build();
        when(userRepository.findByUsername("user1")).thenReturn(User.builder().username("user1").password("$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC").build());

        //when
        InvalidFormulaException e = assertThrows(InvalidFormulaException.class, () ->  calculatorService.calculateAndSaveHistory("user1", requestDto));

        //then
        assertThat(e.getMessage()).isEqualTo("Invalid formula: 1+3+(-6)/(-2)=");
    }

    @Test
    void 연산결과조회 () {
        //given
        Calculator calculator1 = Calculator.builder().username("user1").formula("3+3=6").correctAnswer(6.0).isCorrect(true).build();

        Pageable pageable = PageRequest.of(0,5);

        List<Calculator> pageList = new ArrayList<>();
        pageList.add(calculator1);
        Page<Calculator> pages = new PageImpl<>(pageList);

        when(userRepository.findByUsername("user1")).thenReturn(User.builder().username("user1").password("$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC").build());
        when(calculatorRepository.findByUsername(pageable, "user1")).thenReturn(pages);

        //when
        Page<CalculatorDto> results = calculatorService.inquireCalculatorHistory("user1", pageable);
        CalculatorDto result = results.getContent().get(0);

        //then
        //todo: 질문 - 왜 노랑거 뜨는지 이해 안됨
//        assertThat(result.getCorrectAnswer().equals(calculator1.getCorrectAnswer()));
//        assertThat(result.getFormula().equals(calculator1.getFormula()));

        assertEquals(calculator1.getCorrectAnswer(), result.getCorrectAnswer());
        assertEquals(calculator1.getFormula(), result.getFormula());
    }
}