package com.compu;

import com.compu.patient.model.Patient;
import com.compu.patient.model.PatientDTO;
import com.compu.visit.model.Visit;
import com.compu.visit.model.VisitDTO;
import com.compu.visit.model.VisitRequestDTO;
import com.compu.visit.model.VisitType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class BaseUtilTest {
    public static Visit createVisit(Long id) {
        return Visit.builder()
                .id(id)
                .dateTime(LocalDateTime.now())
                .visitType(VisitType.HOME)
                .reason("Routine checkup")
                .familyHistory("No family history")
                .build();
    }

    public static VisitDTO createVisitDTO() {
        return VisitDTO.builder()
                .id(1L)
                .dateTime(LocalDateTime.now())
                .visitType(VisitType.HOME)
                .reason("Routine checkup")
                .familyHistory("No family history")
                .patientId(1L)
                .build();
    }

    public static VisitRequestDTO createVisitRequestDTO() {
        return VisitRequestDTO.builder()
                .id(1L)
                .dateTime(LocalDateTime.now())
                .visitType(VisitType.HOME)
                .reason("Routine checkup")
                .familyHistory("No family history")
                .patientId(1L)
                .build();
    }


    public static PatientDTO createMockPatientDTO() {
        return PatientDTO.builder()
                .id(1L)
                .name("Test Name")
                .surname("Test Surname")
                .dateOfBirth(LocalDate.now())
                .socialSecurityNumber("123-45-6789")
                .build();
    }
    public static Patient createMockPatient() {
        return Patient.builder()
                .name("Test Name")
                .surname("Test Surname")
                .dateOfBirth(LocalDate.now())
                .socialSecurityNumber("123-45-6789")
                .build();
    }
}
