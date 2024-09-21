package com.example;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@Path("/nonblocking")
public class NonBlockingResource {


    @Inject
    DatabaseSimulationService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<List<String>> getData() {
        return service.getDataNonBlocking(50);
    }
    /*
    @Inject
    NonBlockingService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseData getData() {
        return service.getData();
    }
    */
    /*
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void getDataNonBlocking(@Context SseEventSink eventSink, @Context Sse sse) {
        CompletableFuture.runAsync(() -> {
            try {
                while (!eventSink.isClosed()) {
                    CompletableFuture<String> futureData = service.getDataNonBlocking()
                            .thenComposeAsync(service::enrichData)
                            .thenComposeAsync(service::processData)
                            .thenComposeAsync(service::filterData);

                    String data = futureData.get(1000, TimeUnit.MILLISECONDS);
                    if (data != null) {
                        eventSink.send(sse.newEvent(data));
                    }

                    Thread.sleep(10); // Simulate backpressure by limiting production rate
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                eventSink.close();
            }
        });
    }

     */

}
