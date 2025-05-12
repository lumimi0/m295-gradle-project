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

import com.aprentas.m295.project.m295_rest_api.model.Media;
import com.aprentas.m295.project.m295_rest_api.repository.MediaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test-Klasse für den Media-Controller
 * Testet die CRUD-Operationen des Media-Controllers
 * 
 * @author Luca Minotti
 * @version 1.0
 */
@WebMvcTest(MediaController.class)
public class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MediaRepository mediaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Media testMedia;

    /**
     * Initialisiert die Testdaten vor jedem Test
     */
    @BeforeEach
    public void setUp() {
        testMedia = new Media();
        testMedia.setId(1L);
        testMedia.setTitle("Harry Potter");
        testMedia.setAuthor("J.K. Rowling");
        testMedia.setGenre("Fantasy");
        testMedia.setRating(5);
        testMedia.setIsbnOrEan(9783551557421L);
        testMedia.setFsk(12);
        testMedia.setShelfCode("F-ROW-01");
    }

    /**
     * Testet das Abrufen aller Medien
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testGetAllMedia() throws Exception {
        List<Media> mediaList = Arrays.asList(testMedia);
        
        when(mediaRepository.findAll()).thenReturn(mediaList);

        mockMvc.perform(get("/api/media"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testMedia.getId()))
                .andExpect(jsonPath("$[0].title").value(testMedia.getTitle()))
                .andExpect(jsonPath("$[0].author").value(testMedia.getAuthor()))
                .andExpect(jsonPath("$[0].genre").value(testMedia.getGenre()))
                .andExpect(jsonPath("$[0].rating").value(testMedia.getRating()))
                .andExpect(jsonPath("$[0].isbnOrEan").value(testMedia.getIsbnOrEan()))
                .andExpect(jsonPath("$[0].fsk").value(testMedia.getFsk()))
                .andExpect(jsonPath("$[0].shelfCode").value(testMedia.getShelfCode()));
    }

    /**
     * Testet das Erstellen eines neuen Mediums
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testCreateMedia() throws Exception {
        when(mediaRepository.save(any(Media.class))).thenReturn(testMedia);

        mockMvc.perform(post("/api/media")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMedia)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testMedia.getId()))
                .andExpect(jsonPath("$.title").value(testMedia.getTitle()))
                .andExpect(jsonPath("$.author").value(testMedia.getAuthor()))
                .andExpect(jsonPath("$.genre").value(testMedia.getGenre()))
                .andExpect(jsonPath("$.rating").value(testMedia.getRating()))
                .andExpect(jsonPath("$.isbnOrEan").value(testMedia.getIsbnOrEan()))
                .andExpect(jsonPath("$.fsk").value(testMedia.getFsk()))
                .andExpect(jsonPath("$.shelfCode").value(testMedia.getShelfCode()));
    }

    /**
     * Testet das Aktualisieren eines bestehenden Mediums
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testUpdateMedia() throws Exception {
        Media updatedMedia = new Media();
        updatedMedia.setId(1L);
        updatedMedia.setTitle("Der Herr der Ringe");
        updatedMedia.setAuthor("J.R.R. Tolkien");
        updatedMedia.setGenre("Fantasy");
        updatedMedia.setRating(5);
        updatedMedia.setIsbnOrEan(9783608939842L);
        updatedMedia.setFsk(12);
        updatedMedia.setShelfCode("F-TOL-01");
        
        when(mediaRepository.existsById(1L)).thenReturn(true);
        when(mediaRepository.save(any(Media.class))).thenReturn(updatedMedia);

        mockMvc.perform(put("/api/media/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMedia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedMedia.getId()))
                .andExpect(jsonPath("$.title").value(updatedMedia.getTitle()))
                .andExpect(jsonPath("$.author").value(updatedMedia.getAuthor()))
                .andExpect(jsonPath("$.genre").value(updatedMedia.getGenre()))
                .andExpect(jsonPath("$.rating").value(updatedMedia.getRating()))
                .andExpect(jsonPath("$.isbnOrEan").value(updatedMedia.getIsbnOrEan()))
                .andExpect(jsonPath("$.fsk").value(updatedMedia.getFsk()))
                .andExpect(jsonPath("$.shelfCode").value(updatedMedia.getShelfCode()));
    }

    /**
     * Testet das Löschen eines Mediums
     * 
     * @throws Exception wenn ein Fehler beim Testen auftritt
     */
    @Test
    public void testDeleteMedia() throws Exception {
        when(mediaRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/media/1"))
                .andExpect(status().isNoContent());

        verify(mediaRepository, times(1)).deleteById(1L);
    }
}