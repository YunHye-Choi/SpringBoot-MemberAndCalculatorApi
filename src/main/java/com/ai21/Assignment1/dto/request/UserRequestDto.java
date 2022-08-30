package com.ai21.Assignment1.dto.request;

import com.ai21.Assignment1.dto.service.UserDto;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotNull
    private String username;

    @NotNull
    private String password;

    public static UserDto toUserDto(UserRequestDto userJoinRequestDto) {
        return UserDto.builder()
                .username(userJoinRequestDto.getUsername())
                .password(userJoinRequestDto.getPassword())
                .build();
    }

    public static UserRequestDto toUserRequestDto(UserDto userDto){
        return UserRequestDto.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
    }
}
