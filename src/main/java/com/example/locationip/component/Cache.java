package com.example.locationip.component;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.stereotype.Component;

/** The type Cache. */
@Component
@Data
public class Cache {

  private final Map<String, Object> hashMap =
      new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
          return size() > 15;
        }
      };

  public void putToCache(String key, Object value) {
    hashMap.put(key, value);
  }

  public Object getFromCache(String key) {
    return hashMap.get(key);
  }

  public boolean containsKey(String key) {
    return hashMap.containsKey(key);
  }

  public void removeFromCache(String key) {
    hashMap.remove(key);
  }
}
