package com.gdsc.goldenhour.common;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class GoogleIdTokenProvider {

    @Value("${google.clientId}")
    private String clientId;

    private static final Logger log = Logger.getLogger(GoogleIdTokenProvider.class.getName());

    private GoogleIdTokenVerifier verifier;

    private final UserRepository userRepository;

    public Optional<User> convertUser(String idToken) {
        try {
            if (verifier == null) {
                verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                        .setAudience(Collections.singleton(clientId))
                        .build();
            }

            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken != null) {
                GoogleIdToken.Payload payload = googleIdToken.getPayload();
                String googleId = payload.getSubject();

                // 회원 존재 여부 확인 및 가입
                return Optional.of(
                        userRepository.findById(googleId).orElse(
                                userRepository.save(
                                        User.builder()
                                                .googleId(googleId)
                                                .build()
                                )
                        )
                );
            } else {
                log.warning("Invalid Google ID token.");
                throw new CustomCommonException(ErrorCode.INVALID_TOKEN);
            }
        } catch (GeneralSecurityException | IOException e) {
            log.warning(e.getLocalizedMessage());
        }

        return Optional.empty();
    }



}
