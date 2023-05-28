package com.blaska.authorization;

import org.springframework.stereotype.Service;

@Service
public class DummyAuthorizationService implements AuthorizationService {
    @Override
    public boolean isValidToken(final String token) {
        return true;
    }

    @Override
    public boolean isCsrToken(final String token) {
        return !"usertoken1234".equals(token);
    }

    @Override
    public String getCredentialsFromToken(final String authorizationHeader) {
        if ("usertoken1234".equals(authorizationHeader))
            return "Jérémie Durand";
        return "Sonia Valentin";
    }
}
