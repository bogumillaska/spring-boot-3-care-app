package com.blaska.authorization;

public interface AuthorizationService {
    boolean isValidToken(String token);
    boolean isCsrToken(String token);

    String getCredentialsFromToken(String authorizationHeader);

}
