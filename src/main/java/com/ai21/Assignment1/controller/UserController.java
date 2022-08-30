package com.ai21.Assignment1.controller;

import com.ai21.Assignment1.dto.request.UserRequestDto;
import com.ai21.Assignment1.dto.response.UserResponseDto;
import com.ai21.Assignment1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join (@RequestBody UserRequestDto userJoinRequestDto) {
        return ResponseEntity.ok(UserResponseDto.toUserResponseDto(userService.join(userJoinRequestDto)));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDto> getMyUserInfo(){
        return ResponseEntity.ok(UserResponseDto.toUserResponseDto(userService.getMyUserWithAuthorities()));
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable String username){
        return ResponseEntity.ok(UserResponseDto.toUserResponseDto(userService.getUserWithAuthorities(username)));
    }

    /////////////db login - legacy //////////////
    @GetMapping("user/db-login")
    @ResponseBody
    public ResponseEntity<UserResponseDto> dbLogin (@RequestParam("username") String username, @RequestParam("password") String password) {
        UserRequestDto userRequestDto = new UserRequestDto(username, password);
        return ResponseEntity.ok(UserResponseDto.toUserResponseDto(userService.dbLogin(userRequestDto)));
    }
}
