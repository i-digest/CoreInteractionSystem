package com.coreintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.coreintegration")
public class CoreInteractionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreInteractionSystemApplication.class, args);
    }
}
