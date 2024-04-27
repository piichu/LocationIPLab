package com.example.locationip.component;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

/** The type Request counter. */
@Service
public class RequestCounter {
  private final AtomicInteger counter = new AtomicInteger(0);

  public synchronized int incrementCounter() {
    return counter.incrementAndGet();
  }
}
