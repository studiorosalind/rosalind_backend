package com.rosalind.api;

import com.rosalind.configuration.RosalindCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({RosalindCommonConfig.class})
public class RosalindApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RosalindApiApplication.class, args);
    }
}
