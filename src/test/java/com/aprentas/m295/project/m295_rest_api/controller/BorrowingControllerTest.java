package com.aprentas.m295.project.m295_rest_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aprentas.m295.project.m295_rest_api.model.Address;
import com.aprentas.m295.project.m295_rest_api.model.Borrowing;
import com.aprentas.m295.project.m295_rest_api.model.Customer;
import com.aprentas.m295.project.m295_rest_api.model.Media;
import com.aprentas.m295.project.m295_rest_api.repository.BorrowingRepository;
import com.aprentas.m295.project.m295_rest_api.repository.CustomerRepository;
import com.aprentas.m295.project.m295_rest_api.repository.MediaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Test-Klasse für den Borrowing-Controller
 * Testet die CRUD-Operationen des Borrowing-Controllers
 * 
 * @author Luca Minotti
 * @version 1.4
 * @since 1.0
 */
@WebMvcTest(BorrowingController.class)
public class BorrowingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BorrowingRepository borrowingRepository;
    
    @MockitoBean
    private CustomerRepository customerRepository;
    
    @MockitoBean
    private MediaRepository mediaRepository;

    private ObjectMapper objectMapper;

    private Borrowing testBorrowing;
    private Customer testCustomer;
    private Media testMedia;
    private Address testAddress;

    /**
     * Initialisiert die Testdaten vor jedem Test
     */
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        testAddress = new Address("Musterstrasse", "10", "Musterstadt", "12345");
        testAddress.setId(1L);
        
        testCustomer = new Customer("Max", "Mustermann", LocalDate.of(1990, 1, 1), testAddress, "max@example.com");
        testCustomer.setId(1L);
        
        testMedia = new Media();
        testMedia.setId(1L);
        testMedia.setTitle("Test Buch");
        testMedia.setAuthor("Test Autor");
        
        testBorrowing = new Borrowing();
        testBorrowing.setId(1L);
        testBorrowing.setCustomer(testCustomer);
        testBorrowing.setMedia(testMedia);
        testBorrowing.setDateBorrowed(LocalDate.now());
        testBorrowing.setDueDate(LocalDate.now().plusDays(14));
    }

    /**
     * Testet das Abrufen aller Ausleihen
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testGetAllBorrowings() throws Exception {
        List<Borrowing> borrowings = Arrays.asList(testBorrowing);
        
        when(borrowingRepository.findAll()).thenReturn(borrowings);

        mockMvc.perform(get("/api/borrowings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testBorrowing.getId()))
                .andExpect(jsonPath("$[0].dateBorrowed").exists())
                .andExpect(jsonPath("$[0].dueDate").exists());
    }

    /**
     * Testet das Erstellen einer neuen Ausleihe
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testCreateBorrowing() throws Exception {
        when(customerRepository.findById(testCustomer.getId())).thenReturn(Optional.of(testCustomer));
        when(mediaRepository.findById(testMedia.getId())).thenReturn(Optional.of(testMedia));
        when(borrowingRepository.isMediaBorrowed(testMedia.getId())).thenReturn(false);
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(testBorrowing);

        mockMvc.perform(post("/api/borrowings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBorrowing)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testBorrowing.getId()))
                .andExpect(jsonPath("$.customer.id").value(testCustomer.getId()))
                .andExpect(jsonPath("$.media.id").value(testMedia.getId()));
    }

    /**
     * Testet das Verlängern einer bestehenden Ausleihe
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testExtendBorrowing() throws Exception {
        when(borrowingRepository.existsById(1L)).thenReturn(true);
        when(borrowingRepository.findById(1L)).thenReturn(Optional.of(testBorrowing));
        
        Borrowing extendedBorrowing = new Borrowing();
        extendedBorrowing.setId(1L);
        extendedBorrowing.setCustomer(testCustomer);
        extendedBorrowing.setMedia(testMedia);
        extendedBorrowing.setDateBorrowed(LocalDate.now());
        extendedBorrowing.setDueDate(LocalDate.now().plusDays(14));
        
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(extendedBorrowing);

        mockMvc.perform(put("/api/borrowings/extend/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(extendedBorrowing.getId()))
                .andExpect(jsonPath("$.dateBorrowed").exists())
                .andExpect(jsonPath("$.dueDate").exists());
    }

    /**
     * Testet das Löschen einer Ausleihe
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testDeleteBorrowing() throws Exception {
        when(borrowingRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/borrowings/1"))
                .andExpect(status().isNoContent());

        verify(borrowingRepository, times(1)).deleteById(1L);
    }
}