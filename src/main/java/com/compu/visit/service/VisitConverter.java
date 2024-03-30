package com.compu.visit.service;

import com.compu.patient.service.PatientService;
import com.compu.visit.model.Visit;
import com.compu.visit.model.VisitDTO;
import com.compu.visit.model.VisitRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class VisitConverter {

    @Inject
    PatientService patientService;

    public VisitDTO toDTO(Visit visit) {
        return patientService.getPatientById(visit.getPatienceId())
                .onItem().transform(patient -> VisitDTO.builder()
                        .id(visit.getId())
                        .dateTime(visit.getDateTime())
                        .visitType(visit.getVisitType())
                        .reason(visit.getReason())
                        .familyHistory(visit.getFamilyHistory())
                        .patient(patient)
                        .build())
                .await().indefinitely();
    }

    public Visit toVisit(VisitRequestDTO requestDTO) {
        return Visit.builder()
                .id(requestDTO.getId())
                .dateTime(requestDTO.getDateTime())
                .visitType(requestDTO.getVisitType())
                .reason(requestDTO.getReason())
                .familyHistory(requestDTO.getFamilyHistory())
                .patienceId(requestDTO.getPatientId())
                .build();
    }
}
