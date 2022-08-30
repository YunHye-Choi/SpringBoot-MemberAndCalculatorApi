package com.ai21.Assignment1.enums;

public enum ExceptionMsg {
    EMPTY_VALUE("username 입력된 값이 없습니다."),
    INVALID_USERNAME("username이 유효 형식이 아닙니다."),
    USER_ALREADY_EXIST("이미 존재하는 회원입니다."),
    STRING_LENGTH_OVER("글자수가 초과되었습니다."),
    USER_NOT_EXIST("존재하지 않는 사용자입니다."),
    INCORRECT_PASSWORD("사용자 비밀번호가 틀립니다.");

    private String message;

    ExceptionMsg(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
