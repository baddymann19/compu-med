package com.compu.visit.service;


import com.compu.visit.model.Visit;
import com.compu.visit.model.VisitDTO;
import com.compu.visit.model.VisitRequestDTO;
import com.compu.visit.repo.VisitRepository;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.compu.BaseUtilTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class VisitServiceTest {

    private VisitService visitService;
    private VisitRepository visitRepository;
    private VisitConverter visitConverter;

    @BeforeEach
    public void setup() {
        visitRepository = Mockito.mock(VisitRepository.class);
        visitConverter = Mockito.mock(VisitConverter.class);
        visitService = new VisitService();
        visitService.visitRepository = visitRepository;
        visitService.visitConverter = visitConverter;
    }

    @Test
    public void testGetAllVisits() {
        VisitDTO visitDTO = createVisitDTO();
        when(visitRepository.listAll()).thenReturn(Uni.createFrom().item(Arrays.asList(new Visit())));
        when(visitConverter.toDTO(any(Visit.class))).thenReturn(visitDTO);

        Uni<List<VisitDTO>> result = visitService.getAllVisits();
        assertEquals(visitDTO, result.await().indefinitely().get(0));
    }

    @Test
    public void testGetVisitByIdNotFound() {
        when(visitRepository.findById(anyLong())).thenReturn(Uni.createFrom().nullItem());
        assertThrows(NotFoundException.class, () -> {
            visitService.getVisitById(1L).await().indefinitely();
        });
    }

    @Test
    public void testGetVisitById() {
        VisitDTO visitDTO = createVisitDTO();
        when(visitRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(new Visit()));
        when(visitConverter.toDTO(any(Visit.class))).thenReturn(visitDTO);

        Uni<VisitDTO> result = visitService.getVisitById(1L);
        assertEquals(visitDTO, result.await().indefinitely());
    }

    @Test
    public void testCreateVisit() {
        Visit visit = createVisit(null);
        VisitDTO visitDTO = createVisitDTO();
        VisitRequestDTO visitRequestDTO = createVisitRequestDTO();
        when(visitRepository.persist(any(Visit.class))).thenReturn(Uni.createFrom().item(visit));
        when(visitConverter.toDTO(any(Visit.class))).thenReturn(visitDTO);
        when(visitConverter.toVisit(any(VisitRequestDTO.class))).thenReturn(visit);

        Uni<VisitDTO> result = visitService.createVisit(visitRequestDTO);
        VisitDTO response = result.await().indefinitely();
        assertEquals(visitDTO, response);
        Mockito.verify(visitRepository, Mockito.times(1)).persist(any(Visit.class));
        Mockito.verify(visitConverter, Mockito.times(1)).toDTO(any(Visit.class));
        Mockito.verify(visitConverter, Mockito.times(1)).toVisit(any(VisitRequestDTO.class));
    }

    @Test
    public void testUpdateVisit() {
        Visit visit = createVisit(1L);
        VisitDTO visitDTO = createVisitDTO();
        VisitRequestDTO visitRequestDTO = createVisitRequestDTO();
        when(visitRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(visit));
        when(visitRepository.persist(any(Visit.class))).thenReturn(Uni.createFrom().item(visit));
        when(visitConverter.toDTO(any(Visit.class))).thenReturn(visitDTO);

        Uni<VisitDTO> result = visitService.updateVisit(1L, visitRequestDTO);
        assertEquals(visitDTO, result.await().indefinitely());
        Mockito.verify(visitRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(visitConverter, Mockito.times(1)).toDTO(any(Visit.class));
    }

    @Test
    public void testDeleteVisit() {
        when(visitRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(new Visit()));
        when(visitRepository.deleteById(anyLong())).thenReturn(Uni.createFrom().item(true));

        Uni<Boolean> result = visitService.deleteVisit(1L);
        assertTrue(result.await().indefinitely());
        Mockito.verify(visitRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(visitRepository, Mockito.times(1)).deleteById(anyLong());
    }
}