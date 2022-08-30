package com.ai21.Assignment1.dto.response;

import com.ai21.Assignment1.dto.service.CalculatorDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorResponseDto {


    @NotNull
    private Boolean isCorrect;

//    @NotNull
    @JsonInclude(NON_NULL)
    private Double correctAnswer;

    public static CalculatorResponseDto toCalculatorResponseDto (CalculatorDto calculatorDto) {
        return CalculatorResponseDto.builder()
                .isCorrect(calculatorDto.getIsCorrect())
                .correctAnswer(calculatorDto.getCorrectAnswer())
                .build();
    }
}
