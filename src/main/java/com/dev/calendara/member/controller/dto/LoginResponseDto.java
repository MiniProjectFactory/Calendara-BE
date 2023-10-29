package com.dev.calendara.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto extends SignUpResponseDto {
    private String token;

    @Builder
    public LoginResponseDto(String name, String email, String token) {
        super(name, email);
        this.token = token;
    }
}
