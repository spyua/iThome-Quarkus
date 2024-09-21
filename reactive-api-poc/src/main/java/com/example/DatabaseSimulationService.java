package com.example;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
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

    // Reactive 模式，處理複雜的模擬數據操作
    public Uni<List<String>> getDataReactive(int count) {
        return Uni.createFrom().deferred(() -> {
            List<Uni<String>> unis = IntStream.range(0, count)
                    .mapToObj(i -> simulateComplexDatabaseOperationReactive(i))  // 使用複雜模擬操作
                    .collect(Collectors.toList());

            return Uni.combine().all().unis(unis).with(list -> list.stream()
                    .map(String.class::cast)
                    .collect(Collectors.toList()));
        });
    }

    // Non-blocking 模式，處理複雜的模擬數據操作
    public CompletableFuture<List<String>> getDataNonBlocking(int count) {
        List<CompletableFuture<String>> futures = IntStream.range(0, count)
                .mapToObj(this::simulateComplexDatabaseOperationNonBlocking)  // 使用複雜模擬操作
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    // Blocking 模式，用於測試和比較
    public List<String> getDataBlocking(int count) {
        return IntStream.range(0, count)
                .mapToObj(this::simulateSlowDatabaseOperationBlocking)
                .collect(Collectors.toList());
    }

    // 模擬一個複雜的 Reactive 資料庫操作 + 外部 API 調用
    private Uni<String> simulateComplexDatabaseOperationReactive(int id) {
        return Uni.createFrom().item("Data " + id)
                .onItem().delayIt().by(Duration.ofMillis(100))  // 模擬 100 毫秒延遲
                .flatMap(data -> externalApiCallReactive(id));  // 外部 API 調用
    }

    // 模擬一個複雜的 Non-blocking 資料庫操作 + 外部 API 調用
    private CompletableFuture<String> simulateComplexDatabaseOperationNonBlocking(int id) {
        CompletableFuture<String> future = new CompletableFuture<>();
        vertx.setTimer(100, l -> {
            externalApiCallNonBlocking(id).thenAccept(apiData -> future.complete(apiData));
        });
        return future;
    }

    // 模擬外部 API 調用 - Reactive 版本
    private Uni<String> externalApiCallReactive(int id) {
        return Uni.createFrom().item("API Response for " + id)
                .onItem().delayIt().by(Duration.ofMillis(50));  // 模擬 50 毫秒延遲
    }

    // 模擬外部 API 調用 - Non-blocking 版本
    private CompletableFuture<String> externalApiCallNonBlocking(int id) {
        CompletableFuture<String> future = new CompletableFuture<>();
        vertx.setTimer(50, l -> future.complete("API Response for " + id));
        return future;
    }

    // Blocking 資料庫操作模擬
    private String simulateSlowDatabaseOperationBlocking(int id) {
        try {
            Thread.sleep(100);  // 模擬 100 毫秒延遲
            return "Data " + id;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error for " + id;
        }
    }
    /*
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

     */

}
