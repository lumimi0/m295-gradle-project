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
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aprentas.m295.project.m295_rest_api.model.Address;
import com.aprentas.m295.project.m295_rest_api.model.Customer;
import com.aprentas.m295.project.m295_rest_api.repository.AddressRepository;
import com.aprentas.m295.project.m295_rest_api.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Test-Klasse für den Customer-Controller
 * Testet die CRUD-Operationen des Customer-Controllers
 * 
 * @author Luca Minotti
 * @version 1.5
 * @since 1.0
 */
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerRepository customerRepository;
    
    @MockitoBean
    private AddressRepository addressRepository;

    private ObjectMapper objectMapper;

    private Customer testCustomer;
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
    }

    /**
     * Testet das Abrufen eines Kunden anhand seiner ID
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testGetCustomerById() throws Exception {
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        mockMvc.perform(get("/api/customers/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCustomer.getId()))
                .andExpect(jsonPath("$.firstName").value(testCustomer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testCustomer.getLastName()))
                .andExpect(jsonPath("$.email").value(testCustomer.getEmail()));
    }

    /**
     * Testet das Erstellen eines neuen Kunden
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testCreateCustomer() throws Exception {
        when(addressRepository.existsById(1L)).thenReturn(true);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testCustomer.getId()))
                .andExpect(jsonPath("$.firstName").value(testCustomer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testCustomer.getLastName()))
                .andExpect(jsonPath("$.email").value(testCustomer.getEmail()));
    }

    /**
     * Testet das Aktualisieren eines bestehenden Kunden
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testUpdateCustomer() throws Exception {
        Customer updatedCustomer = new Customer("Maxi", "Musterfrau", LocalDate.of(1990, 1, 1), testAddress, "maxi@example.com");
        updatedCustomer.setId(1L);
        
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCustomer.getId()))
                .andExpect(jsonPath("$.firstName").value(updatedCustomer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedCustomer.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedCustomer.getEmail()));
    }

    /**
     * Testet das Löschen eines Kunden
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testDeleteCustomer() throws Exception {
        when(customerRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerRepository, times(1)).deleteById(1L);
    }
}