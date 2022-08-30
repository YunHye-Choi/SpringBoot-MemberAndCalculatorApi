package com.ai21.Assignment1.dto.service;

import com.ai21.Assignment1.domain.Calculator;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorDto {
    private String username;

    @NotNull
    private String formula;

//    @NotNull
    private Double correctAnswer;

    @NotNull
    private Boolean isCorrect;

    public static CalculatorDto toCalculatorDto(Calculator calculator) {
        return CalculatorDto.builder()
                .formula(calculator.getFormula())
                .isCorrect(calculator.getIsCorrect())
                .correctAnswer(calculator.getCorrectAnswer())
                .build();
    }

    public static Calculator toCalculator(CalculatorDto calculatorDto) {
        return Calculator.builder()
                .username(calculatorDto.getUsername())
                .formula(calculatorDto.getFormula())
                .correctAnswer(calculatorDto.getCorrectAnswer())
                .isCorrect(calculatorDto.getIsCorrect())
                .build();
    }

    public static Page<CalculatorDto> toDtoPage(Page<Calculator> calculators) {
        Page<CalculatorDto> calculatorDtos = calculators.map( m -> CalculatorDto.builder()
                        .isCorrect(m.getIsCorrect())
                        .correctAnswer(m.getCorrectAnswer())
                        .formula(m.getFormula())
                        .username(m.getUsername())
                        .build());
        return calculatorDtos;
    }

}
