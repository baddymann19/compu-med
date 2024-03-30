package com.compu.patient.controller;

import com.compu.patient.model.PatientDTO;
import com.compu.patient.model.PatientRequestDTO;
import com.compu.patient.service.PatientService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

import static jakarta.ws.rs.core.Response.Status.*;

@Path("/patient")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientResource {

    @Inject
    PatientService patientService;

    @POST
    public Uni<RestResponse<PatientDTO>> createPatient(@Valid PatientRequestDTO requestDTO) {
        return patientService.createPatient(requestDTO)
                .map(createdPatient -> RestResponse.status(CREATED, createdPatient));
    }

    @GET
    public Uni<RestResponse<List<PatientDTO>>> getAllPatients() {
        return patientService.getAllPatients()
                .map(patients -> RestResponse.status(OK, patients));
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<PatientDTO>> getPatientById(@PathParam("id") Long id) {
        return patientService.getPatientById(id)
                .map(patient -> RestResponse.status(OK, patient))
                .onItem().ifNull().continueWith(RestResponse.status(NOT_FOUND));
    }

    @PUT
    @Path("/{id}")
    public Uni<RestResponse<PatientDTO>> updatePatient(@PathParam("id") Long id, @Valid PatientRequestDTO requestDTO) {
        return patientService.updatePatient(id, requestDTO)
                .map(updatedPatient -> RestResponse.status(OK, updatedPatient));
    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<Void>> deletePatient(@PathParam("id") Long id) {
        return patientService.deletePatient(id)
                .map(deleted -> deleted ? RestResponse.status(OK) : RestResponse.status(NOT_FOUND));
    }
}
