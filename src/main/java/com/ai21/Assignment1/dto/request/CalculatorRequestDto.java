package com.ai21.Assignment1.dto.request;

import com.ai21.Assignment1.dto.service.CalculatorDto;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorRequestDto {
    @NotNull
    private String formula;

    public static CalculatorDto toCalculatorDto (CalculatorRequestDto requestDto) {
        return CalculatorDto.builder()
                .formula(requestDto.getFormula())
                .build();
    }
}
