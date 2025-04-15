package com.rosalind.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rosalind.api", "com.rosalind.common"})
@EntityScan("com.rosalind.common.domain")
@EnableJpaRepositories("com.rosalind.common.repository")
public class RosalindApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RosalindApiApplication.class, args);
    }
}
