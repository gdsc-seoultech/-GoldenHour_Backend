package com.gdsc.goldenhour.common;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
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
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class GoogleIdTokenProvider {

    @Value("${google.clientId}")
    private String clientId;

    private static final Logger log = Logger.getLogger(GoogleIdTokenProvider.class.getName());

    private GoogleIdTokenVerifier verifier;

    public String provideGoogleId(String idToken) {
        if (idToken == null) {
            throw new CustomCommonException(ErrorCode.MISSING_TOKEN);
        }

        try {
            checkVerifier();
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                log.warning("Invalid Google ID token.");
                throw new CustomCommonException(ErrorCode.INVALID_TOKEN);
            }

            return googleIdToken.getPayload().getSubject();
        } catch (GeneralSecurityException | IOException e) {
            throw new CustomCommonException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

    private void checkVerifier() {
        if (this.verifier == null) {
            this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singleton(clientId))
                    .build();
        }
    }

}
