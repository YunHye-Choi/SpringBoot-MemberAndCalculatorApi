package com.ai21.Assignment1.service;

import com.ai21.Assignment1.domain.Calculator;
import com.ai21.Assignment1.dto.request.CalculatorRequestDto;
import com.ai21.Assignment1.dto.service.CalculatorDto;
import com.ai21.Assignment1.enums.ExceptionMsg;
import com.ai21.Assignment1.exception.InvalidFormulaException;
import com.ai21.Assignment1.exception.NotFoundUserException;
import com.ai21.Assignment1.repository.CalculatorRepository;
import com.ai21.Assignment1.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalculatorService {
    private static CalculatorRepository calculatorRepository;

    private static UserRepository userRepository;

    public CalculatorService (CalculatorRepository calculatorRepository, UserRepository userRepository) {
        this.calculatorRepository = calculatorRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CalculatorDto calculateAndSaveHistory(String username, CalculatorRequestDto requestDto){
        if (userRepository.findByUsername(username)==null) {
            throw new NotFoundUserException(ExceptionMsg.USER_NOT_EXIST.getMessage());
        }
        String formula = requestDto.getFormula();

        /*계산 로직*/
        String[] splitFormula = formula.split("=");
        if(splitFormula.length != 2) {
            throw new InvalidFormulaException("Invalid formula: " + formula);
        }
        Double expectedAnswer = Double.parseDouble(splitFormula[1]);
        String formulaNoEqual = splitFormula[0];
        Double correctAnswer = calculateCore(formulaNoEqual);

        CalculatorDto calculatorDto;
        if(expectedAnswer.equals(correctAnswer)) {
            calculatorDto = CalculatorDto.builder()
                    .username(username)
                    .formula(formula)
                    .isCorrect(true)
                    .build();
        }else {
            calculatorDto = CalculatorDto.builder()
                    .username(username)
                    .formula(formula)
                    .isCorrect(false)
                    .correctAnswer(correctAnswer)
                    .build();
        }

        /*계산 로직 끝*/

        calculatorRepository.save(CalculatorDto.toCalculator(calculatorDto));
        return calculatorDto;
    }

    @Transactional(readOnly = true)
    public Page<CalculatorDto> inquireCalculatorHistory(String username, Pageable pageable) {
        if (userRepository.findByUsername(username)==null) {
            throw new NotFoundUserException(ExceptionMsg.USER_NOT_EXIST.getMessage());
        }

        Page<Calculator> resultEntities = calculatorRepository.findByUsername(pageable, username);
        Page<CalculatorDto> calculatorDtos = CalculatorDto.toDtoPage(resultEntities);


        return calculatorDtos;
    }

    private static double calculateCore(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new InvalidFormulaException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new InvalidFormulaException("Unknown function: " + func);
                } else {
                    throw new InvalidFormulaException("Unexpected: " + ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

}