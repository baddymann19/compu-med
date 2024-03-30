package com.compu.patient.controller;


import com.compu.patient.model.PatientDTO;
import com.compu.patient.model.PatientRequestDTO;
import com.compu.patient.service.PatientService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;

import java.util.Collections;

import static com.compu.BaseUtilTest.createMockPatientDTO;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@QuarkusTest
public class PatientResourceTest {

    private PatientService patientService;

    @BeforeEach
    public void setup() {
        patientService = Mockito.mock(PatientService.class);
        QuarkusMock.installMockForType(patientService, PatientService.class);

    }

    @Test
    public void testGetAllPatients() {
        PatientDTO mockPatientDTO = createMockPatientDTO();
        when(patientService.getAllPatients()).thenReturn(Uni.createFrom().item(Collections.singletonList(mockPatientDTO)));

        given()
                .when().get("/patient")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("[0].surname", is(mockPatientDTO.getSurname()))
                .body("[0].name", is(mockPatientDTO.getName()))
                .body("[0].dateOfBirth", is(mockPatientDTO.getDateOfBirth().toString()))
                .body("[0].socialSecurityNumber", is(mockPatientDTO.getSocialSecurityNumber()));
    }

    @Test
    public void testGetPatientById() {
        PatientDTO mockPatientDTO = createMockPatientDTO();
        when(patientService.getPatientById(1L)).thenReturn(Uni.createFrom().item(mockPatientDTO));

        given()
                .when().get("/patient/{id}", 1L)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("surname", is(mockPatientDTO.getSurname()))
                .body("name", is(mockPatientDTO.getName()))
                .body("dateOfBirth", is(mockPatientDTO.getDateOfBirth().toString()))
                .body("socialSecurityNumber", is(mockPatientDTO.getSocialSecurityNumber()));
    }

    @Test
    public void testGetPatientById_NotFound() {
        when(patientService.getPatientById(1L)).thenThrow(new NotFoundException("Visit not found"));

        given()
                .when().get("/patient/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreatePatient() {
        PatientDTO mockPatientDTO = createMockPatientDTO();
        when(patientService.createPatient(any(PatientRequestDTO.class))).thenReturn(Uni.createFrom().item(mockPatientDTO));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new PatientRequestDTO())
                .when().post("/patient")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("surname", is(mockPatientDTO.getSurname()))
                .body("name", is(mockPatientDTO.getName()))
                .body("dateOfBirth", is(mockPatientDTO.getDateOfBirth().toString()))
                .body("socialSecurityNumber", is(mockPatientDTO.getSocialSecurityNumber()));
    }

    @Test
    public void testUpdatePatient() {
        PatientDTO mockPatientDTO = createMockPatientDTO();
        when(patientService.updatePatient(anyLong(), any(PatientRequestDTO.class))).thenReturn(Uni.createFrom().item(mockPatientDTO));

        given()
                .contentType(ContentType.JSON)
                .body(new PatientRequestDTO())
                .when().put("/patient/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("surname", is(mockPatientDTO.getSurname()))
                .body("name", is(mockPatientDTO.getName()))
                .body("dateOfBirth", is(mockPatientDTO.getDateOfBirth().toString()))
                .body("socialSecurityNumber", is(mockPatientDTO.getSocialSecurityNumber()));
    }

    @Test
    public void testDeletePatient() {
        when(patientService.deletePatient(anyLong())).thenReturn(Uni.createFrom().item(true));

        given()
                .when().delete("/patient/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

}