package com.rosalind.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.rosalind.common.domain")
public class RosalindApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RosalindApiApplication.class, args);
    }
}
