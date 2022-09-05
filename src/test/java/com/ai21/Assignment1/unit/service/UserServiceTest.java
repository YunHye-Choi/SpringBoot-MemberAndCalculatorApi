package com.ai21.Assignment1.unit.service;

import com.ai21.Assignment1.domain.Authority;
import com.ai21.Assignment1.domain.User;
import com.ai21.Assignment1.dto.request.UserRequestDto;
import com.ai21.Assignment1.dto.service.UserDto;
import com.ai21.Assignment1.enums.ExceptionMsg;
import com.ai21.Assignment1.repository.UserRepository;
import com.ai21.Assignment1.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void 회원가입() {
        // given
        UserDto userDto = UserDto.builder()
                .username("joinuser1")
                .password("pwpwpw1")
                .build();

        UserRequestDto userRequestDto = UserRequestDto.toUserRequestDto(userDto);

        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC");

        // when
        UserDto result = userService.join(UserRequestDto.toUserRequestDto(userDto));

        // then
        assertThat(userDto.getUsername()).isEqualTo(result.getUsername());
    }

    @Test
    void 회원가입실패_중복회원예약(){
        //given
        UserDto userDto = UserDto.builder()
                .username("joinuser1")
                .password("pwpwpw1")
                .build();

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(User.builder().build());

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(UserRequestDto.toUserRequestDto(userDto)));

        //then
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



    /*/////////// legacy - DB 로그인(JWT 적용 x) //////////////
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

        // mock 객체는 null을 return 하므로 이 부분이 포함된다고 볼 수 있다
        // when(userRepository.findByUsername("user1")).thenReturn(null);

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.dbLogin(UserRequestDto.toUserRequestDto(userDto)));

        //then
        assertThat(e.getMessage()).isEqualTo(ExceptionMsg.USER_NOT_EXIST.getMessage());
    }

    @Test
    void DB로그인실패_비밀번호불일치(){
        //given
        UserDto userDto = UserDto.builder()
                .username("user1")
                .password("correctPW")
                .build();

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        when(userRepository.findByUsername("user1"))
                .thenReturn(
                        User.builder()
                                .username("user1")
                                .password("1234")
                                .authorities(Collections.singleton(authority))
                                .build()
                );

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.dbLogin(UserRequestDto.toUserRequestDto(userDto)));

        //then
        assertThat(e.getMessage()).isEqualTo(ExceptionMsg.INCORRECT_PASSWORD.getMessage());
    }*/
}