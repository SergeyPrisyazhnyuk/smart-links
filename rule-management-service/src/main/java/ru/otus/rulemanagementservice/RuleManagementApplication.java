package ru.otus.rulemanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"ru.otus"})
public class RuleManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuleManagementApplication.class, args);
    }
}
