package com.dev.calendara.auth.service;

import com.dev.calendara.auth.user.GoogleUser;
import com.dev.calendara.auth.user.ProviderUser;
import com.dev.calendara.member.Member;
import com.dev.calendara.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {

    private final UserService userService;
    private final MemberRepository userRepository;

    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
//        User user = userRepository.findByUsername(providerUser.getUsername());
        Optional<Member> user = userRepository.findById(1L);

        if (user.isEmpty()) {
            ClientRegistration clientRegistration = userRequest.getClientRegistration();
            userService.register(clientRegistration.getRegistrationId(), providerUser);
        } else {
            log.info("userRequest = {}", userRequest);
        }
    }

    public ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        String registrationId = clientRegistration.getRegistrationId();
        if (registrationId.equals("google")) {
            return new GoogleUser(oAuth2User, clientRegistration);
        }
        return null;
    }
}
