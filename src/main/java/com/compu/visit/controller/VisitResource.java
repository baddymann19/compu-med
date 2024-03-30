package com.compu.visit.controller;

import com.compu.visit.model.VisitDTO;
import com.compu.visit.model.VisitRequestDTO;
import com.compu.visit.service.VisitService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

import static jakarta.ws.rs.core.Response.Status.*;

@Path("/visits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisitResource {

    @Inject
    VisitService visitService;

    @GET
    public Uni<RestResponse<List<VisitDTO>>> getAllVisits() {
        return visitService.getAllVisits()
                .map(patients -> RestResponse.status(OK, patients));
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<VisitDTO>> getVisitById(@PathParam("id") Long id) {
        return visitService.getVisitById(id)
                .map(patient -> RestResponse.status(OK, patient));
    }

    @POST
    public Uni<RestResponse<VisitDTO>> createVisit(@Valid VisitRequestDTO requestDTO) {
        return visitService.createVisit(requestDTO)
                .map(createdVisit -> RestResponse.status(CREATED, createdVisit));
    }

    @PUT
    @Path("/{id}")
    public Uni<RestResponse<VisitDTO>> updateVisit(@PathParam("id") Long id, @Valid VisitRequestDTO requestDTO) {
        return visitService.updateVisit(id, requestDTO)
                .map(updatedVisit -> RestResponse.status(OK, updatedVisit));

    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<Void>> deleteVisit(@PathParam("id") Long id) {
        return visitService.deleteVisit(id)
                .map(deleted -> deleted ? RestResponse.status(OK) : RestResponse.status(NOT_FOUND));
    }
}
