package com.compu.patient.service;

import com.compu.patient.model.Patient;
import com.compu.patient.model.PatientDTO;
import com.compu.patient.model.PatientRequestDTO;
import com.compu.patient.repo.PatientRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.junit5.VertxExtension;
import io.vertx.mutiny.core.Vertx;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(VertxExtension.class)
public class PatientServiceTest {

    private PatientService patientService;
    private PatientRepository patientRepository;
    private PatientConverter patientConverter;

    private Vertx vertx;

    @BeforeEach
    public void setup() {
        patientRepository = Mockito.mock(PatientRepository.class);
        patientConverter = Mockito.mock(PatientConverter.class);
        patientService = new PatientService();
        vertx = Vertx.vertx();
        patientService.patientRepository = patientRepository;
        patientService.patientConverter = patientConverter;
    }

//    @Test
//    public void testGetAllPatients() {
//        PatientDTO patientDTO = new PatientDTO();
//        Uni<List<Patient>> item = Uni.createFrom().item(Collections.singletonList(createMockPatient()));
//        when(patientRepository.findAll()).thenAnswer(invocation -> {} Uni.createFrom().item(Collections.singletonList(createMockPatient())));
//       // when(patientConverter.toDTO(any(Patient.class))).thenReturn(patientDTO);
//        Uni<List<PatientDTO>> result = patientService.getAllPatients();
//        assertEquals(1, result.await().indefinitely().size());
//    }

    @Test
    public void testGetPatientByIdNotFound() {
        when(patientRepository.findById(anyLong())).thenReturn(Uni.createFrom().nullItem());
        assertThrows(NotFoundException.class, () -> {
            patientService.getPatientById(1L).await().indefinitely();
        });
    }

    @Test
    public void testGetPatientById() {
        PatientDTO patientDTO = new PatientDTO();
        when(patientRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(createMockPatient()));
        when(patientConverter.toDTO(any(Patient.class))).thenReturn(patientDTO);

        Uni<PatientDTO> result = patientService.getPatientById(1L);
        assertEquals(patientDTO, result.await().indefinitely());
    }

    @Test
    public void testCreatePatient() {
        Patient patient = createMockPatient();
        PatientDTO patientDTO = createMockPatientDTO();
        when(patientRepository.persist(any(Patient.class))).thenReturn(Uni.createFrom().item(createMockPatient()));
        when(patientConverter.toDTO(any(Patient.class))).thenReturn(createMockPatientDTO());
        when(patientConverter.toEntity(any(PatientRequestDTO.class))).thenReturn(patient);

        Uni<PatientDTO> result = patientService.createPatient(new PatientRequestDTO());
        PatientDTO response = result.await().indefinitely();
        assertEquals(patientDTO, response);
        Mockito.verify(patientRepository, Mockito.times(1)).persist(any(Patient.class));
        Mockito.verify(patientConverter, Mockito.times(1)).toDTO(any(Patient.class));
        Mockito.verify(patientConverter, Mockito.times(1)).toEntity(any(PatientRequestDTO.class));

    }

    @Test
    public void testUpdatePatient() {
        vertx.runOnContext(() -> {
            Patient patient = createMockPatient();
            PatientDTO patientDTO = createMockPatientDTO();
            when(patientRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(patient));
            when(patientRepository.persist(any(Patient.class))).thenReturn(Uni.createFrom().item(patient));
            when(patientConverter.toDTO(any(Patient.class))).thenReturn(patientDTO);

            Uni<PatientDTO> result = patientService.updatePatient(1L, new PatientRequestDTO());
            assertEquals(patientDTO, result.await().indefinitely());
            Mockito.verify(patientRepository, Mockito.times(1)).findById(any(Long.class));
            Mockito.verify(patientRepository, Mockito.times(1)).persist(any(Patient.class));
            Mockito.verify(patientConverter, Mockito.times(1)).toDTO(any(Patient.class));
        });

    }

    @Test
    public void testDeletePatient() {
        when(patientRepository.deleteById(anyLong())).thenReturn(Uni.createFrom().item(true));

        Uni<Boolean> result = patientService.deletePatient(1L);
        assertTrue(result.await().indefinitely());
    }

    private Patient createMockPatient() {
        return Patient.builder()
                .id(1L)
                .name("Test Name")
                .surname("Test Surname")
                .dateOfBirth(LocalDate.now())
                .socialSecurityNumber("123-45-6789")
                .build();
    }

    private PatientDTO createMockPatientDTO() {
        Patient mockPatient = createMockPatient();
        return PatientDTO.builder()
                .id(mockPatient.getId())
                .name(mockPatient.getName())
                .surname(mockPatient.getSurname())
                .dateOfBirth(mockPatient.getDateOfBirth())
                .socialSecurityNumber(mockPatient.getSocialSecurityNumber())
                .build();
    }
}