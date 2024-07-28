package com.ibar.metrocard.utility;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IdempotencyKeyService {
    private final Map<String, Object> keys = new HashMap<>();

    public boolean isKeyUsed(String key) {
        return keys.containsKey(key);
    }

    public Object useKey(String key, Object response) {
        keys.put(key, response);
        return response;
    }

    public Object getResponse(String key) {
        return keys.get(key);
    }
}
