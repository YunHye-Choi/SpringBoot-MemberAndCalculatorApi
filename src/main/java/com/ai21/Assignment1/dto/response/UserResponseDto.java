package com.ai21.Assignment1.dto.response;

import com.ai21.Assignment1.domain.Authority;
import com.ai21.Assignment1.dto.service.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    @NotNull
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    private Set<Authority> authoritySet;

    public static UserResponseDto toUserResponseDto(UserDto userDto){
        return UserResponseDto.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .authoritySet(userDto.getAuthoritySet().stream()
                        .map(authority -> Authority.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
