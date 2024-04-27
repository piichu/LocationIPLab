package com.example.locationip.component;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** The type Logging aspect. */
@Aspect
@Component
public class LoggingAspect {
  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
  private final RequestCounter counter = new RequestCounter();

  @Pointcut("execution(* com.example.locationip.controller.*.create*(..))")
  public void create() {}

  @Pointcut("execution(* com.example.locationip.controller.*.delete*(..))")
  public void delete() {}

  @Pointcut("execution(* com.example.locationip.controller.*.update*(..))")
  public void update() {}

  @Pointcut("execution(* com.example.locationip.controller.LocationController.*(..))")
  public void countLocation() {}

  @AfterReturning(pointcut = "create()", returning = "result")
  public void logCreate(Object result) {
    logger.info("Created: {}", result);
  }

  @AfterReturning(pointcut = "delete()", returning = "result")
  public void logDelete(Object result) {
    logger.info("Deleted: {}", result);
  }

  @AfterReturning(pointcut = "update()", returning = "result")
  public void logUpdate(Object result) {
    logger.info("Updated: {}", result);
  }

  @AfterReturning(pointcut = "countLocation()")
  public void logCountLocation() {
    logger.info("Location service count: {}", counter.incrementCounter());
  }
}
