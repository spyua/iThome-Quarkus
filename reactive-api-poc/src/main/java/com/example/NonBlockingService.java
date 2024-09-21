package com.example;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class NonBlockingService {

    @Inject
    ManagedExecutor executor;

    private Random random = new Random();

    public CompletableFuture<String> getDataNonBlocking() {
        return CompletableFuture.supplyAsync(() -> "Item " + System.currentTimeMillis(), executor);
    }

    public CompletableFuture<String> enrichData(String data) {
        return CompletableFuture.supplyAsync(() -> {
            simulateRandomDelay(50, 150);
            return data + " (Enriched with " + random.nextInt(1000) + ")";
        }, executor);
    }

    public CompletableFuture<String> processData(String data) {
        return CompletableFuture.supplyAsync(() -> {
            simulateRandomDelay(100, 300);
            // Simulate complex processing
            String processed = data;
            for (int i = 0; i < 1000; i++) {
                processed = processed.replaceAll(String.valueOf(i), String.valueOf(random.nextInt(1000)));
            }
            return processed + " (Processed)";
        }, executor);
    }

    public CompletableFuture<String> filterData(String data) {
        return CompletableFuture.supplyAsync(() -> {
            simulateRandomDelay(50, 150);
            return data.contains("500") ? null : data;
        }, executor);
    }

    private void simulateRandomDelay(int minDelay, int maxDelay) {
        try {
            Thread.sleep(random.nextInt(maxDelay - minDelay) + minDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
