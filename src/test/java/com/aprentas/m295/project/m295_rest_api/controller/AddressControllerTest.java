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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aprentas.m295.project.m295_rest_api.model.Address;
import com.aprentas.m295.project.m295_rest_api.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test-Klasse für den Address-Controller
 * Testet die CRUD-Operationen des Address-Controllers
 * 
 * @author Luca Minotti
 * @version 1.4
 * @sicne 1.0
 */
@WebMvcTest(AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Address testAddress;

    /**
     * Initialisiert die Testdaten vor jedem Test
     */
    @BeforeEach
    public void setUp() {
        testAddress = new Address("Musterstrasse", "10", "Musterstadt", "12345");
        testAddress.setId(1L);
    }

    /**
     * Testet das Abrufen aller Adressen
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testGetAllAddresses() throws Exception {
        List<Address> addresses = Arrays.asList(testAddress);
        
        when(addressRepository.findAll()).thenReturn(addresses);

        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testAddress.getId()))
                .andExpect(jsonPath("$[0].street").value(testAddress.getStreet()))
                .andExpect(jsonPath("$[0].streetNumber").value(testAddress.getStreetNumber()))
                .andExpect(jsonPath("$[0].city").value(testAddress.getCity()))
                .andExpect(jsonPath("$[0].zip").value(testAddress.getZip()));
    }

    /**
     * Testet das Erstellen einer neuen Adresse
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testCreateAddress() throws Exception {
        Address newAddress = new Address("Neustrasse", "20", "Neustadt", "54321");
        
        when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

        mockMvc.perform(post("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAddress)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value(newAddress.getStreet()))
                .andExpect(jsonPath("$.streetNumber").value(newAddress.getStreetNumber()))
                .andExpect(jsonPath("$.city").value(newAddress.getCity()))
                .andExpect(jsonPath("$.zip").value(newAddress.getZip()));
    }

    /**
     * Testet das Aktualisieren einer bestehenden Adresse
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testUpdateAddress() throws Exception {
        Address updatedAddress = new Address("Updatestrasse", "30", "Updatestadt", "67890");
        updatedAddress.setId(1L);
        
        when(addressRepository.existsById(1L)).thenReturn(true);
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);

        mockMvc.perform(put("/api/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedAddress.getId()))
                .andExpect(jsonPath("$.street").value(updatedAddress.getStreet()))
                .andExpect(jsonPath("$.streetNumber").value(updatedAddress.getStreetNumber()))
                .andExpect(jsonPath("$.city").value(updatedAddress.getCity()))
                .andExpect(jsonPath("$.zip").value(updatedAddress.getZip()));
    }

    /**
     * Testet das Löschen einer Adresse
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testDeleteAddress() throws Exception {
        when(addressRepository.existsById(1L)).thenReturn(true);
        when(addressRepository.isAddressReferenced(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/addresses/1"))
                .andExpect(status().isNoContent());

        verify(addressRepository, times(1)).deleteById(1L);
    }
}