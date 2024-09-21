package com.example;

import io.smallrye.mutiny.Multi;
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
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> getDataReactive() {
        return service.getDataReactiveStream()
                .onItem().transformToUniAndMerge(item -> service.enrichData(item))
                .onItem().transformToUniAndMerge(item -> service.processData(item))
                .onItem().transformToUniAndMerge(item -> service.filterData(item))
                .onOverflow().drop();
    }

}
