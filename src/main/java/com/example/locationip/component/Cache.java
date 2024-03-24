package com.example.locationip.component;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Data
public class Cache {

    private final Map<String, Object> hashMap = new LinkedHashMap<>();

    public void putToCache(String key, Object value) {
        hashMap.put(key, value);
    }

    public Object getFromCache(String key) {
        return hashMap.get(key);
    }
    public boolean containsKey(String key){
        return hashMap.containsKey(key);
    }

    public void removeFromCache(String key) { hashMap.remove(key); }

}