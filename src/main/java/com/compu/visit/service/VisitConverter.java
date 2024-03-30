package com.compu.visit.service;

import com.compu.visit.model.Visit;
import com.compu.visit.model.VisitDTO;
import com.compu.visit.model.VisitRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class VisitConverter {

    @Inject
    ObjectMapper objectMapper;

    public VisitDTO toDTO(Visit visit) {
        return objectMapper.convertValue(visit, VisitDTO.class);
    }

    public Visit toVisit(VisitRequestDTO requestDTO) {
        return objectMapper.convertValue(requestDTO, Visit.class);
    }
}
