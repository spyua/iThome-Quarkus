package com.example;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DatabaseSimulationService {

    @Inject
    Vertx vertx;

    public Uni<List<String>> getDataReactive(int count) {
        return Uni.createFrom().deferred(() -> {
            List<Uni<String>> unis = IntStream.range(0, count)
                    .mapToObj(i -> simulateSlowDatabaseOperationReactive(i))
                    .toList();

            // 使用強制轉型來確保類型一致
            return Uni.combine().all().unis(unis).with(list -> list.stream().map(String.class::cast).collect(Collectors.toList()));
        });
    }



    public CompletableFuture<List<String>> getDataNonBlocking(int count) {
        List<CompletableFuture<String>> futures = IntStream.range(0, count)
                .mapToObj(this::simulateSlowDatabaseOperationNonBlocking)
                .toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    public List<String> getDataBlocking(int count) {
        return IntStream.range(0, count)
                .mapToObj(this::simulateSlowDatabaseOperationBlocking)
                .toList();
    }

    private Uni<String> simulateSlowDatabaseOperationReactive(int id) {
        return Uni.createFrom().emitter(em ->
                vertx.setTimer(100, l -> em.complete("Data " + id))
        );
    }

    private CompletableFuture<String> simulateSlowDatabaseOperationNonBlocking(int id) {
        CompletableFuture<String> future = new CompletableFuture<>();
        vertx.setTimer(100, l -> future.complete("Data " + id));
        return future;
    }

    private String simulateSlowDatabaseOperationBlocking(int id) {
        try {
            Thread.sleep(100);
            return "Data " + id;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error for " + id;
        }
    }

}
