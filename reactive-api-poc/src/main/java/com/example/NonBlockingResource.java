package com.example;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/nonblocking")
public class NonBlockingResource {

    @Inject
    NonBlockingService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<List<String>> getDataNonBlocking() {
        return service.getDataNonBlocking();
    }

}
