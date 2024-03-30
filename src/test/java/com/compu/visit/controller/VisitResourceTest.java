package com.compu.visit.controller;


import com.compu.visit.model.VisitDTO;
import com.compu.visit.model.VisitRequestDTO;
import com.compu.visit.model.VisitType;
import com.compu.visit.service.VisitService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;


@QuarkusTest
public class VisitResourceTest {

    @Spy
    private VisitService visitService;

    @BeforeEach
    public void setup() {
        visitService = Mockito.mock(VisitService.class);
        QuarkusMock.installMockForType(visitService, VisitService.class);

    }

    @Test
    public void testGetAllVisitsEndpoint() {
        VisitDTO visitDTO = createVisitDTO();
        when(visitService.getAllVisits()).thenReturn(Uni.createFrom().item(Collections.singletonList(createVisitDTO())));
        given()
                .when().get("/visits")
                .then()
                .statusCode(200)
                .log().all()
                .body("[0].reason", is(visitDTO.getReason()))
                .body("[0].visitType", is(visitDTO.getVisitType().toString()));
    }

    @Test
    public void testGetVisitByIdEndpoint() {
        VisitDTO visitDTO = createVisitDTO();
        when(visitService.getVisitById(anyLong())).thenReturn(Uni.createFrom().item(visitDTO));

        given()
                .when().get("/visits/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("reason", is(visitDTO.getReason()))
                .body("visitType", is(visitDTO.getVisitType().toString()));
    }

    @Test
    public void testGetVisitByIdEndpoint_NotFound() {
        when(visitService.getVisitById(anyLong())).thenThrow(new NotFoundException("Visit not found"));

        given()
                .when().get("/visits/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreateVisitEndpoint() {
        VisitDTO visitDTO = createVisitDTO();
        when(visitService.createVisit(any(VisitRequestDTO.class))).thenReturn(Uni.createFrom().item(visitDTO));

        given()
                .contentType(ContentType.JSON)
                .body(createVisitRequestDTO())
                .when().post("/visits")
                .then()
                .statusCode(201)
                .body("reason", is(visitDTO.getReason()))
                .body("visitType", is(visitDTO.getVisitType().toString()));
    }

    @Test
    public void testUpdateVisitEndpoint() {
        when(visitService.updateVisit(anyLong(), any(VisitRequestDTO.class))).thenReturn(Uni.createFrom().item(createVisitDTO()));

        given()
                .contentType(ContentType.JSON)
                .body(createVisitRequestDTO())
                .when().put("/visits/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteVisitEndpoint() {
        when(visitService.deleteVisit(anyLong())).thenReturn(Uni.createFrom().item(true));

        given()
                .when().delete("/visits/1")
                .then()
                .statusCode(200);
    }

    private VisitDTO createVisitDTO() {
        return VisitDTO.builder()
                .id(1L)
                .dateTime(LocalDateTime.now())
                .visitType(VisitType.HOME)
                .reason("Routine checkup")
                .familyHistory("No family history")
                .build();
    }

    private VisitRequestDTO createVisitRequestDTO() {
        return VisitRequestDTO.builder()
                .id(1L)
                .dateTime(LocalDateTime.now())
                .visitType(VisitType.HOME)
                .reason("Routine checkup")
                .familyHistory("No family history")
                .build();
    }
}
