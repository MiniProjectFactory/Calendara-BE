package com.dev.calendara.member.controller.dto;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    String email;
    String password;
    String name;
}
