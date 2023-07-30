package com.microservices.products.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {
    public static String asJsonString(final Object object) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String result = mapper.writeValueAsString(object);
            return result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
