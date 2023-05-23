package com.blaska.authorization;

public interface AuthorizationService {
    boolean isValidToken(String token);

    String getClientIdFromToken(String authorizationHeader);
}
