package com.ai21.Assignment1.dto.service;

import com.ai21.Assignment1.domain.Authority;
import com.ai21.Assignment1.domain.User;
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
public class UserDto {
    @NotNull
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    public static User toUser (UserDto userDto){
        return User.builder()
                .username(userDto.username)
                .password(userDto.password)
                .build();
    }
    private Set<Authority> authoritySet;

    public static UserDto toUserDto(User user) {
        if(user == null) return null;

        return UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authoritySet(user.getAuthorities().stream()
                        .map(authority -> Authority.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }

}
