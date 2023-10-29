package com.dev.calendara.member;

import com.dev.calendara.member.controller.dto.LoginRequestDto;
import com.dev.calendara.member.controller.dto.LoginResponseDto;
import com.dev.calendara.member.controller.dto.SignUpRequestDto;
import com.dev.calendara.member.controller.dto.SignUpResponseDto;
import com.dev.calendara.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    private final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        logger.info("[signup] 회원가입 요청. email : {}", requestDto.getEmail());
        return memberService.signUp(requestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(LoginRequestDto requestDto) {
        logger.info("[login] 로그인 요청. email : {}", requestDto.getEmail());
        return memberService.login(requestDto);
    }
}
