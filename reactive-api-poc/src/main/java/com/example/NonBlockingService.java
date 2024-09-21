package com.example;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class NonBlockingService {

    @Inject
    ManagedExecutor executor;

    public CompletionStage<List<String>> getDataNonBlocking() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return IntStream.range(0, 1000)
                    .mapToObj(i -> "Item " + i)
                    .collect(Collectors.toList());
        }, executor);
    }
}
