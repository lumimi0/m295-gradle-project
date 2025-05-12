package com.aprentas.m295.project.m295_rest_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aprentas.m295.project.m295_rest_api.model.Media;
import com.aprentas.m295.project.m295_rest_api.repository.MediaRepository;

import jakarta.transaction.Transactional;

/**
 * REST-Controller für Medien-Operationen
 * Stellt Endpunkte zum Verwalten von Medien bereit
 * 
 * @author Luca Minotti
 * @version 1.2
 * @since 1.0
 */
@RestController
@RequestMapping("/api/media")
@CrossOrigin("**")
public class MediaController {

    private final MediaRepository mediaRepository;
    
    @Autowired    
	public MediaController(MediaRepository mediaRepository) {
		super();
		this.mediaRepository = mediaRepository;
	}

	/**
	 * Gibt alle Medien zurück
	 * 
	 * @return Eine Liste aller Medien
	 */
    @GetMapping
    public ResponseEntity<List<Media>> getAllMedia() {
    	List<Media> media = this.mediaRepository.findAll();
    	return ResponseEntity.ok(media);
    }
    
    /**
     * Gibt ein Medium anhand seiner ID zurück
     * 
     * @param id - Die ID des gesuchten Mediums
     * @return Das Medium mit der zugehörigen ID oder ein 404-Fehler, wenn die ID nicht existiert
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Media>> getMediaById(@PathVariable Long id) {
    	if (!this.mediaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
    	
    	Optional<Media> media = this.mediaRepository.findById(id);
        return ResponseEntity.ok(media);
    }
    
    /**
     * Gibt Medien anhand ihres Titels zurück
     * 
     * @param id - Der Titel des gesuchten Mediums
     * @return Eine Liste von Medien mit dem zugehörigen Titel
     */
    @GetMapping("/{title}")
    public ResponseEntity<List<Media>> getMediaByTitle(@PathVariable String title) {
    	List<Media> media = this.mediaRepository.findByTitle(title);
        return ResponseEntity.ok(media);
    }
    
    /**
     * Erstellt ein neues Medium
     * 
     * @param media - Die Daten des neuen Mediums
     * @return Das erstellte Medium
     */
    @PostMapping
    public ResponseEntity<Media> createMedia(@RequestBody Media media) {
    	Media savedMedia = this.mediaRepository.save(media);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedia);
    }
    
    /**
     * Aktualisiert ein bestehendes Medium
     * 
     * @param id - Die ID des zu aktualisierenden Mediums
     * @param media- Die Daten des aktualisierten Mediums
     * @return - Das aktualisierte Medium oder ein 404-Fehler, wenn es nicht existiert
     */
    @PutMapping("/{id}")
    public ResponseEntity<Media> updateMedia(@PathVariable Long id, Media media) {
    	if (!this.mediaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
    	
        media.setId(id);
        Media updatedMedia = this.mediaRepository.save(media);
        return ResponseEntity.ok(updatedMedia);
    }
    
    /**
     * Löscht ein Medium anhand seiner ID
     * 
     * @param id - Die ID des zu löschenden Mediums
     * @return Ein 204-Status bei Erfolg, oder ein 404-Fehler, wenn es nicht existiert
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
    	if (!this.mediaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
    	
        this.mediaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}