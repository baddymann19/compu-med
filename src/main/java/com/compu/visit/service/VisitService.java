package com.compu.visit.service;

import com.compu.visit.model.Visit;
import com.compu.visit.model.VisitDTO;
import com.compu.visit.model.VisitRequestDTO;
import com.compu.visit.repo.VisitRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VisitService {

    @Inject
    VisitRepository visitRepository;

    @Inject
    VisitConverter visitConverter;

    @WithTransaction
    public Uni<List<VisitDTO>> getAllVisits() {
        return visitRepository.listAll().onItem().transform(visits -> visits.stream()
                .map(visitConverter::toDTO)
                .collect(Collectors.toList()));
    }

    @WithTransaction
    public Uni<VisitDTO> getVisitById(Long id) {
        return visitRepository.findById(id)
                .onItem().ifNull().failWith(new NotFoundException("Visit not found with id: " + id))
                .onItem().transform(visitConverter::toDTO);
    }

    @WithTransaction
    public Uni<VisitDTO> createVisit(VisitRequestDTO requestDTO) {
        Visit visit = visitConverter.toVisit(requestDTO);
        return visitRepository.persist(visit).onItem().transform(visitConverter::toDTO);
    }

    @WithTransaction
    public Uni<VisitDTO> updateVisit(Long id, VisitRequestDTO requestDTO) {
        return visitRepository.findById(id)
                .onItem().ifNull().failWith(new NotFoundException("Visit not found with id: " + id))
                .onItem().ifNotNull()
                .transform(entity -> {
                    entity.setVisitType(requestDTO.getVisitType());
                    entity.setReason(requestDTO.getReason());
                    entity.setDateTime(requestDTO.getDateTime());
                    entity.setFamilyHistory(requestDTO.getFamilyHistory());
                    entity.setPatientId(requestDTO.getPatientId());
                    return entity;
                }).map(visitConverter::toDTO);
    }

    @WithTransaction
    public Uni<Boolean> deleteVisit(Long id) {
        return visitRepository.findById(id)
                .onItem().ifNull().failWith(new NotFoundException("Visit not found with id: " + id))
                .onItem().transformToUni(existingVisit -> visitRepository.deleteById(id));
    }
}