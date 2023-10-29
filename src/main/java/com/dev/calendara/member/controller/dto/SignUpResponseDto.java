package com.dev.calendara.member.controller.dto;

import com.dev.calendara.member.Member;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SignUpResponseDto {
    private String name;
    private String email;

    public SignUpResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static SignUpResponseDto of(Member member) {
        return new SignUpResponseDto(member.getName(), member.getEmail());
    }
}
