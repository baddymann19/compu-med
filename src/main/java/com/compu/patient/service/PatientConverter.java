package com.compu.patient.service;

import com.compu.patient.model.Patient;
import com.compu.patient.model.PatientDTO;
import com.compu.patient.model.PatientRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PatientConverter {

    @Inject
    ObjectMapper objectMapper;

    public Patient toEntity(PatientRequestDTO patientDTO) {
        return objectMapper.convertValue(patientDTO, Patient.class);
    }

    public PatientDTO toDTO(Patient patient) {
        return objectMapper.convertValue(patient, PatientDTO.class);
    }

}
