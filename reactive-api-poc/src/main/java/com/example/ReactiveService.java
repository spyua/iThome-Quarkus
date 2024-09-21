package com.example;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@ApplicationScoped
public class ReactiveService {

    @Inject
    Vertx vertx;

    private Random random = new Random();

    public Uni<ResponseData> getData() {
        return Uni.createFrom().item(() -> {
            // 模拟I/O操作，例如数据库查询
            simulateIOOperation();
            return new ResponseData("Reactive Response");
        }).runSubscriptionOn(Infrastructure.getDefaultExecutor());
    }

    private void simulateIOOperation() {
        try {
            Thread.sleep(100); // 模拟延迟
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //

    public Multi<String> getDataReactiveStream() {
        return Multi.createFrom().emitter(emitter -> {
            long timerId = vertx.setPeriodic(10, id -> {
                if (emitter.requested() > 0) {
                    emitter.emit("Item " + System.currentTimeMillis());
                }
            });
            emitter.onTermination(() -> vertx.cancelTimer(timerId));
        });
    }

    public Uni<String> enrichData(String data) {
        return Uni.createFrom().item(() -> {
            simulateRandomDelay(50, 150);
            return data + " (Enriched with " + random.nextInt(1000) + ")";
        });
    }

    public Uni<String> processData(String data) {
        return Uni.createFrom().item(() -> {
            simulateRandomDelay(100, 300);
            // Simulate complex processing
            String processed = data;
            for (int i = 0; i < 1000; i++) {
                processed = processed.replaceAll(String.valueOf(i), String.valueOf(random.nextInt(1000)));
            }
            return processed + " (Processed)";
        });
    }

    public Uni<String> filterData(String data) {
        return Uni.createFrom().item(() -> {
            simulateRandomDelay(50, 150);
            return data.contains("500") ? null : data;
        });
    }

    private void simulateRandomDelay(int minDelay, int maxDelay) {
        try {
            Thread.sleep(random.nextInt(maxDelay - minDelay) + minDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //
}
