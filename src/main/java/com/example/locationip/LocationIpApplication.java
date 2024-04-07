package com.example.locationip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/** The type Location ip application. */
@SpringBootApplication
@EnableJpaAuditing
@EnableWebMvc
public class LocationIpApplication {

  public static void main(String[] args) {
    SpringApplication.run(LocationIpApplication.class, args);
  }
}
