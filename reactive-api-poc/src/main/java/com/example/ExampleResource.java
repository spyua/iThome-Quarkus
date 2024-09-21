package com.example;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/test")
public class ExampleResource {

    @Inject
    ExternalService externalService;

    // 響應式端點
    @GET
    @Path("/reactive")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> reactiveHello() {
        return externalService.callExternalApi()
                .onItem().transform(response -> "Hello from Reactive! Response: " + response);
    }

    // 非阻塞端點
    @GET
    @Path("/nonblocking")
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> nonBlockingHello() {
        return CompletableFuture.supplyAsync(() -> {
            // 模擬一個外部 API 的調用
            String response = externalService.callExternalApiSync();
            return "Hello from Non-blocking! Response: " + response;
        });
    }
}
