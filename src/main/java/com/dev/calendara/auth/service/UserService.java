package com.dev.calendara.auth.service;

import com.dev.calendara.auth.user.ProviderUser;
import com.dev.calendara.member.Member;
import com.dev.calendara.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository userRepository;

    public void register(String registrationId, ProviderUser providerUser) {

//        User user = User.builder().registrationId(registrationId)
//                .id(providerUser.getId())
//                .username(providerUser.getUsername())
//                .password(providerUser.getPassword())
//                .authorities(providerUser.getAuthorities())
//                .provider(providerUser.getProvider())
//                .email(providerUser.getEmail())
//                .build();
        Member user = Member.builder().build();
        userRepository.save(user);

//        userRepository.register(user);
    }
}
