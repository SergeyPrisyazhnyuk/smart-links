package ru.otus.routingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"ru.otus"})
public class RoutingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoutingServiceApplication.class, args);
    }
}