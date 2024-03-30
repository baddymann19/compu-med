package com.compu.visit.service;

import com.compu.visit.model.Visit;
import com.compu.visit.model.VisitDTO;
import com.compu.visit.model.VisitRequestDTO;
import com.compu.visit.repo.PatientRepository;
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
    PatientRepository patientRepository;

    @Inject
    VisitConverter visitConverter;

    @WithTransaction
    public Uni<List<VisitDTO>> getAllVisits() {
        return patientRepository.listAll().onItem().transform(visits -> visits.stream()
                .map(visitConverter::toDTO)
                .collect(Collectors.toList()));
    }

    @WithTransaction
    public Uni<VisitDTO> getVisitById(Long id) {
        return patientRepository.findById(id)
                .onItem().ifNull().failWith(new NotFoundException("Visit not found with id: " + id))
                .onItem().transform(visitConverter::toDTO);
    }

    @WithTransaction
    public Uni<VisitDTO> createVisit(VisitRequestDTO requestDTO) {
        Visit visit = visitConverter.toVisit(requestDTO);
        return patientRepository.persist(visit).onItem().transform(visitConverter::toDTO);
    }

    @WithTransaction
    public Uni<VisitDTO> updateVisit(Long id, VisitRequestDTO requestDTO) {
        return patientRepository.findById(id)
                .onItem().ifNull().failWith(new NotFoundException("Visit not found with id: " + id))
                .onItem().transformToUni(existingVisit -> {
                    Visit updatedVisit = visitConverter.toVisit(requestDTO);
                    return patientRepository.persist(updatedVisit).map(visitConverter::toDTO);
                });
    }

    @WithTransaction
    public Uni<Boolean> deleteVisit(Long id) {
        return patientRepository.findById(id)
                .onItem().ifNull().failWith(new NotFoundException("Visit not found with id: " + id))
                .onItem().transformToUni(existingVisit -> patientRepository.deleteById(id));
    }
}