//package com.example.patient.service;
//
//import com.example.patient.model.Patient;
//import com.example.patient.repo.PatientRepository;
//import io.smallrye.mutiny.Uni;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.internal.stubbing.answers.Returns;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class PatientServiceTest1 {
//
//    @Mock
//    private PatientRepository patientRepository;
//
//    @InjectMocks
//    private PatientService patientService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void createPatientSuccessfully() {
//        Patient patient = new Patient();
//        when(patientRepository.persist(patient)).thenReturn(Uni.createFrom().item(patient));
//
//        Uni<Patient> result = patientService.createPatient(patient);
//
//        assertNotNull(result);
//        verify(patientRepository, times(1)).persist(patient);
//    }
//
//    @Test
//    public void getAllPatientsReturnsEmptyListWhenNoPatients() {
//        when(patientRepository.findAll().list()).thenReturn(Uni.createFrom().item(Collections.emptyList()));
//
//        Uni<List<Patient>> result = patientService.getAllPatients();
//
//        assertNotNull(result);
//        assertTrue(result.await().indefinitely().isEmpty());
//        verify(patientRepository, times(1)).findAll().list();
//    }
//
//    @Test
//    public void getPatientByIdReturnsPatientWhenExists() {
//        Patient patient = new Patient();
//        when(patientRepository.findById(1L)).thenReturn(Uni.createFrom().item(patient));
//
//        Uni<Patient> result = patientService.getPatientById(1L);
//
//        assertNotNull(result);
//        assertEquals(patient, result.await().indefinitely());
//        verify(patientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void updatePatientReturnsFalseWhenNoUpdate() {
//        Patient patient = new Patient();
//        when(patientRepository.update(anyString(), any(Object[].class))).thenReturn(Uni.createFrom().item(0));
//
//        Uni<Patient> result = patientService.updatePatient(1L, patient);
//
//        assertNotNull(result);
//        assertNull(result.await().indefinitely());
//        verify(patientRepository, times(1)).update(anyString(), any(Object[].class));
//    }
//
//    @Test
//    public void deletePatientReturnsTrueWhenDeleted() {
//        when(patientRepository.deleteById(1L)).thenAnswer(new Returns(Uni.createFrom().item(1)));
//
//        Uni<Boolean> result = patientService.deletePatient(1L);
//
//        assertNotNull(result);
//        assertTrue(result.await().indefinitely());
//        verify(patientRepository, times(1)).deleteById(1L);
//    }
//}