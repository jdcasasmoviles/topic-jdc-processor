package com.example.infrastructure.config;

import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.Executors;

@ApplicationScoped
@Startup
public class VirtualThreadConfig {

    @PostConstruct
    public void init() {
        // Configurar virtual threads para Mutiny
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        // Usar virtual threads para el pool por defecto
        Infrastructure.setDefaultExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}