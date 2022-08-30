package com.ai21.Assignment1.controller;

import com.ai21.Assignment1.dto.request.CalculatorRequestDto;
import com.ai21.Assignment1.dto.response.CalculatorHistoryResponseDto;
import com.ai21.Assignment1.dto.response.CalculatorResponseDto;
import com.ai21.Assignment1.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CalculatorController {
    private final CalculatorService calculatorService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/calculate")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CalculatorResponseDto> calculate (@RequestBody CalculatorRequestDto calculatorRequestDto) {
        String username = getUsernameFromContext();
        return ResponseEntity.ok(CalculatorResponseDto.toCalculatorResponseDto(calculatorService.calculateAndSaveHistory(username, calculatorRequestDto)) );
    }

    /*@GetMapping("/calculate-history")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CalculatorHistoryResponseDto>> calculateHistory(){
        return ResponseEntity.ok(calculatorService.inquireCalculatorHistory());
    }*/

    @GetMapping("/calculate-history")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<CalculatorHistoryResponseDto>> calculateHistory(Pageable pageable){
        String username = getUsernameFromContext();
        Page<CalculatorHistoryResponseDto> responseDtos = CalculatorHistoryResponseDto.toCalculatorHistoryResponseDtoPage(calculatorService.inquireCalculatorHistory(username, pageable));
        return ResponseEntity.ok(responseDtos);
    }

    private static String getUsernameFromContext () {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }
}
