package com.compu.patient.service;
import com.compu.patient.model.Patient;
import com.compu.patient.model.PatientDTO;
import com.compu.patient.model.PatientRequestDTO;
import com.compu.patient.repo.PatientRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class PatientService {

    @Inject
    PatientRepository patientRepository;

    @Inject
    PatientConverter patientConverter;

    @WithTransaction
    public Uni<PatientDTO> createPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = patientConverter.toEntity(patientRequestDTO);
        return patientRepository.persist(patient)
                .onItem().transform(patientConverter::toDTO);
    }

    @WithTransaction
    public Uni<List<PatientDTO>> getAllPatients() {
        return patientRepository.findAll().list()
                .onItem().transform(patients -> patients.stream()
                        .map(patientConverter::toDTO)
                        .collect(Collectors.toList()));
    }

    @WithTransaction
    public Uni<PatientDTO> getPatientById(Long id) {
        return patientRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Patient not found with id: " + id))
                .onItem().transform(patientConverter::toDTO);
    }

    @WithTransaction
    public Uni<PatientDTO> updatePatient(Long id, PatientRequestDTO requestDTO) {
        return Panache
                .withTransaction(() -> patientRepository.findById(id)
                        .onItem().ifNull().failWith(() -> new NotFoundException("Patient not found with id: " + id))
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.setName(requestDTO.getName());
                            entity.setSurname(requestDTO.getSurname());
                            entity.setDateOfBirth(requestDTO.getDateOfBirth());
                            entity.setSocialSecurityNumber(requestDTO.getSocialSecurityNumber());
                            return entity;
                        })
                        .map(patientConverter::toDTO)
                        .onFailure().recoverWithNull());
    }

    @WithTransaction
    public Uni<Boolean> deletePatient(Long id) {
        return patientRepository.deleteById(id);
    }
}
