package com.ai21.Assignment1.service;

import com.ai21.Assignment1.domain.Authority;
import com.ai21.Assignment1.domain.User;
import com.ai21.Assignment1.dto.request.UserRequestDto;
import com.ai21.Assignment1.dto.service.UserDto;
import com.ai21.Assignment1.enums.ExceptionMsg;
import com.ai21.Assignment1.exception.NotFoundUserException;
import com.ai21.Assignment1.repository.UserRepository;
import com.ai21.Assignment1.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static UserRepository userRepository;
    private static PasswordEncoder passwordEncoder;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto join(UserRequestDto userRequestDto) {
        UserDto userDto = UserRequestDto.toUserDto(userRequestDto);
        isValidUsername(userDto);
        String username = userDto.getUsername();

        // empty string check
        if(username.equals("")){
            throw new IllegalStateException("[username] " + ExceptionMsg.EMPTY_VALUE.getMessage());
        }

        // 길이 제한
        if(username.length() > 255) {
            throw new IllegalStateException("[username] " + ExceptionMsg.STRING_LENGTH_OVER.getMessage());
        }

        // 숫자, 영어만 허용
        String regex = "^[0-9a-zA-Z]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new IllegalStateException(ExceptionMsg.INVALID_USERNAME.getMessage());
        }

        if(isDuplicateUser(userDto)){
            throw new IllegalStateException(ExceptionMsg.USER_ALREADY_EXIST.getMessage());
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .authorities(Collections.singleton(authority))
                .build();

        userRepository.save(user);
        return UserDto.toUserDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.toUserDto(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities(){
        return UserDto.toUserDto(SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow( () -> new NotFoundUserException("Member Not Found")));
    }

    @Transactional
    public UserDto dbLogin(UserRequestDto userRequestDto) {
        UserDto loginUserDto = UserRequestDto.toUserDto(userRequestDto);
        User userInfo = userRepository.findByUsername(loginUserDto.getUsername());
        if (userInfo == null) {
            throw new IllegalStateException(ExceptionMsg.USER_NOT_EXIST.getMessage());
        }
        UserDto userDtoInfo = UserDto.toUserDto(userInfo);
        if (!loginUserDto.getPassword().equals(userInfo.getPassword())) {
            throw new IllegalStateException(ExceptionMsg.INCORRECT_PASSWORD.getMessage());
        }
        return userDtoInfo;
    }

    private Boolean isDuplicateUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            return true;
        }
        else return false;
    }

    private void isValidUsername (UserDto userDto) {
        String username = userDto.getUsername();

        // empty string check
        if(username.equals("")){
            throw new IllegalStateException("[username] " + ExceptionMsg.EMPTY_VALUE.getMessage());
        }

        // 길이 제한
        if(username.length() > 255) {
            throw new IllegalStateException("[username] " + ExceptionMsg.STRING_LENGTH_OVER.getMessage());
        }

        // 숫자, 영어만 허용
        String regex = "^[0-9a-zA-Z]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new IllegalStateException(ExceptionMsg.INVALID_USERNAME.getMessage());
        }
    }
}
