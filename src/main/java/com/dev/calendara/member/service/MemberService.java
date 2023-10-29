package com.dev.calendara.member.service;

import com.dev.calendara.common.exception.custom.BusinessException;
import com.dev.calendara.common.exception.dto.ErrorMessage;
import com.dev.calendara.config.security.JwtTokenProvider;
import com.dev.calendara.member.Member;
import com.dev.calendara.member.controller.dto.LoginRequestDto;
import com.dev.calendara.member.controller.dto.LoginResponseDto;
import com.dev.calendara.member.controller.dto.SignUpRequestDto;
import com.dev.calendara.member.controller.dto.SignUpResponseDto;
import com.dev.calendara.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("[loadUserByUsername] starts loadUserByUsername. username : {}", email);
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(email));
    }

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        logger.info("[signUp] 회원 가입 member email : ", requestDto.getEmail());
        Member saved = memberRepository.save(
                Member.builder()
                        .email(requestDto.getEmail())
                        .name(requestDto.getName())
                        .createdAt(LocalDateTime.now())
                        .build());
        return SignUpResponseDto.of(saved);
    }

    public LoginResponseDto login(LoginRequestDto requestDto) throws RuntimeException {
        logger.info("[login] signDataHandler 로 회원 정보 요청");
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_MEMBER_EMAIL));
        logger.info("[login] email : {}", member.getEmail());

        logger.info("[login] 패스워드 비교 수행");
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorMessage.NOT_FOUND_MEMBER_EMAIL);
        }
        logger.info("[login] 패스워드 일치");
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .name(requestDto.getEmail())
                .email(requestDto.getEmail())
                .token(jwtTokenProvider.createToken(member.getEmail()))
                .build();

        return loginResponseDto;
    }
}
