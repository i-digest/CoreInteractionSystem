package com.coreintegration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.coreintegration")
public class CoreInteractionSystemApplication {
}
