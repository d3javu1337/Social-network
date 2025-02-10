package org.d3javu.bd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BdApplication {

    public static void main(String[] args) {
        SpringApplication.run(BdApplication.class, args);
    }

}
