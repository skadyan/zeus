package com.cumulativeminds.zeus.compute;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComputeServiceBeans {

    @Bean
    public static ExecutorService dataComputeProcessor() {
        return Executors.newFixedThreadPool(5);
    }
}
