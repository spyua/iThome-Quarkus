package com.example;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
@Path("/reactive")
public class ReactiveResource {
    @Inject
    ReactiveService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<String>> getDataReactive() {
        return service.getDataReactive();
    }
}
