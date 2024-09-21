package com.example;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@ApplicationScoped
public class ReactiveService {

    @Inject
    Vertx vertx;

    public Uni<List<String>> getDataReactive() {
        return Uni.createFrom().emitter(emitter -> {
            vertx.setTimer(100, id -> {
                List<String> result = IntStream.range(0, 1000)
                        .mapToObj(i -> "Item " + i)
                        .collect(Collectors.toList());
                emitter.complete(result);
            });
        });
    }

}
