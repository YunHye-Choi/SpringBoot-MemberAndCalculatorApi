package com.ai21.Assignment1.dto.response;

import com.ai21.Assignment1.domain.Calculator;
import com.ai21.Assignment1.dto.service.CalculatorDto;
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
public class CalculatorHistoryResponseDto {
    @NotNull
    private Boolean isCorrect;

    @NotNull
    private String formula;

    public static CalculatorHistoryResponseDto toCalculatorHistoryResponseDto (CalculatorDto calculatorDto) {
        return CalculatorHistoryResponseDto.builder()
                .isCorrect(calculatorDto.getIsCorrect())
                .formula(calculatorDto.getFormula())
                .build();
    }

    public static Page<CalculatorHistoryResponseDto> toCalculatorHistoryResponseDtoPage(Page<CalculatorDto> calculatorDtos) {
        Page<CalculatorHistoryResponseDto> pages = calculatorDtos.map( m -> CalculatorHistoryResponseDto.builder()
                .isCorrect(m.getIsCorrect())
                .formula(m.getFormula())
                .build());
        return pages;
    }
}
