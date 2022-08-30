package com.ai21.Assignment1.integration.service;

import com.ai21.Assignment1.domain.User;
import com.ai21.Assignment1.dto.request.UserRequestDto;
import com.ai21.Assignment1.dto.service.UserDto;
import com.ai21.Assignment1.enums.ExceptionMsg;
import com.ai21.Assignment1.repository.UserRepository;
import com.ai21.Assignment1.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 회원가입() {
        // given
        UserDto userDto = new UserDto();
        userDto.setUsername("joinuser1");
        userDto.setPassword("pwpwpw1");

        // when
        UserDto result = userService.join(UserRequestDto.toUserRequestDto(userDto));

        // then
        User findUser = userRepository.findByUsername(result.getUsername());
        assertThat(UserDto.toUserDto(findUser).getUsername()).isEqualTo(result.getUsername());
    }

    @Test
    void 회원가입실패_중복회원예약(){
        //given
        UserDto userDto1 = UserDto.builder()
                .username("test2")
                .password("password2")
                .build();
        UserDto userDto2 = UserDto.builder()
                .username("test2")
                .password("password2")
                .build();

        //when
        userService.join(UserRequestDto.toUserRequestDto(userDto1));

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(UserRequestDto.toUserRequestDto(userDto2)));
        assertThat(e.getMessage()).isEqualTo(ExceptionMsg.USER_ALREADY_EXIST.getMessage());
    }

    @Test
    void 회원가입실패_username_알파벳숫자_외의_문자포함(){
        //given
        UserDto userDto1 = UserDto.builder()
                .username("hey!@#123")
                .password("password123")
                .build();

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(UserRequestDto.toUserRequestDto(userDto1)));

        //then
        assertThat(e.getMessage()).isEqualTo(ExceptionMsg.INVALID_USERNAME.getMessage());
    }

    @Test
    void 회원가입실패_username_빈문자열값(){
        //given
        UserDto userDto1 = UserDto.builder()
                .username("")
                .password("password123")
                .build();

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(UserRequestDto.toUserRequestDto(userDto1)));

        //then
        assertThat(e.getMessage()).isEqualTo("[username] " + ExceptionMsg.EMPTY_VALUE.getMessage());
    }

    @Test
    void 회원가입실패_username_길이초과(){
        //given
        String un = "";
        for(int i = 0 ; i < 256; i++){
            un += "k";
        }
        UserDto userDto1 = UserDto.builder()
                .username(un)
                .password("password123")
                .build();

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(UserRequestDto.toUserRequestDto(userDto1)));

        //then
        assertThat(e.getMessage()).isEqualTo("[username] " + ExceptionMsg.STRING_LENGTH_OVER.getMessage());
    }



    /////////// legacy - DB 로그인(JWT 적용 x) //////////////
    @Test
    void DB로그인(){
        //given
        UserDto userDto= UserDto.builder()
                .username("springtest")
                .password("spsp123")
                .build();

        //when
        UserDto result = userService.dbLogin(UserRequestDto.toUserRequestDto(userDto));

        // then
        User findUser = userRepository.findByUsername(result.getUsername());
        assertThat(UserDto.toUserDto(findUser).getUsername()).isEqualTo(result.getUsername());
        assertThat(UserDto.toUserDto(findUser).getPassword()).isEqualTo(result.getPassword());
    }

    @Test
    void DB로그인실패_존재하지않는회원(){
        //given
        UserDto userDto= UserDto.builder()
                .username("ailenuser")
                .password("spsp123")
                .build();

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.dbLogin(UserRequestDto.toUserRequestDto(userDto)));

        //then
        assertThat(e.getMessage()).isEqualTo(ExceptionMsg.USER_NOT_EXIST.getMessage());

    }

    @Test
    void DB로그인실패_비밀번호불일치(){
        //given
        UserDto userDto = UserDto.builder()
                .username("user")
                .password("incorrectPW")
                .build();

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.dbLogin(UserRequestDto.toUserRequestDto(userDto)));

        //then
        assertThat(e.getMessage()).isEqualTo(ExceptionMsg.INCORRECT_PASSWORD.getMessage());
    }
}
