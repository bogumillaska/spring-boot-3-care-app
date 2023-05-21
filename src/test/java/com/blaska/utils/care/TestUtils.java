package com.blaska.utils.care;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}