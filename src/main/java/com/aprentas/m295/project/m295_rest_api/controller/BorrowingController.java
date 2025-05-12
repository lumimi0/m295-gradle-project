package com.aprentas.m295.project.m295_rest_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aprentas.m295.project.m295_rest_api.model.Borrowing;
import com.aprentas.m295.project.m295_rest_api.model.Customer;
import com.aprentas.m295.project.m295_rest_api.model.Media;
import com.aprentas.m295.project.m295_rest_api.repository.BorrowingRepository;
import com.aprentas.m295.project.m295_rest_api.repository.CustomerRepository;
import com.aprentas.m295.project.m295_rest_api.repository.MediaRepository;

import jakarta.transaction.Transactional;

/**
 * REST-Controller für Ausleihe-Operationen
 * Stellt Endpunkte zum Verwalten von Ausleihen bereit
 * 
 * @author Luca Minotti
 * @version 1.2
 * @since 1.0
 */
@RestController
@RequestMapping("/api/borrowings")
@CrossOrigin("**")
public class BorrowingController {

	private final BorrowingRepository borrowingRepository;
	
    private final CustomerRepository customerRepository;
    
    private final MediaRepository mediaRepository;
    
    @Autowired
    public BorrowingController(BorrowingRepository borrowingRepository, CustomerRepository customerRepository,
			MediaRepository mediaRepository) {
		super();
		this.borrowingRepository = borrowingRepository;
		this.customerRepository = customerRepository;
		this.mediaRepository = mediaRepository;
	}

	/**
     * Gibt alle Ausleihen zurück
     *
     * @return Eine Liste aller Ausleihen
     */
    @GetMapping
    public ResponseEntity<List<Borrowing>> getAllBorrowings() {
        List<Borrowing> borrowings = this.borrowingRepository.findAll();
        return ResponseEntity.ok(borrowings);
    }

    /**
     * Gibt eine Ausleihe anhand ihrer ID zurück.
     *
     * @param id - Die ID der gesuchten Ausleihe
     * @return Die gefundene Ausleihe oder ein 404-Fehler, wenn nicht gefunden
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Borrowing>> getBorrowingById(@PathVariable Long id) {
    	if (!this.borrowingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
    	
    	Optional<Borrowing> borrowing = this.borrowingRepository.findById(id);
    	return ResponseEntity.ok(borrowing);
    } 
	
    /**
     * Erstellt eine neue Ausleihe
     * 
     * @param borrowing - Die Daten der neuen Ausleihe
     * @return Die erstellte Ausleihe
     */
	@PostMapping
	public ResponseEntity<Borrowing> createBorrowing(@RequestBody Borrowing borrowing) {
		if (borrowing.getCustomer() == null || borrowing.getCustomer().getId() == null ||
			borrowing.getMedia() == null || borrowing.getMedia().getId() == null) {
	        return ResponseEntity.badRequest().build();
	    }
		
		Optional<Customer> customer = this.customerRepository.findById(borrowing.getCustomer().getId());
        Optional<Media> media = this.mediaRepository.findById(borrowing.getMedia().getId());
        
        if (customer.isEmpty() || media.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        if (this.borrowingRepository.isMediaBorrowed(media.get().getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        Borrowing newBorrowing = new Borrowing(customer.get(), media.get());
        Borrowing savedBorrowing = this.borrowingRepository.save(newBorrowing);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBorrowing);
	}
	
	/**
	 * Verlängert eine Ausleihe
	 * Setzt das Ausleihdatum auf das aktuelle Datum und aktualisiert das Fälligkeitsdatum
	 * 
	 * @param id - Die ID der zu verlängernden Ausleihe
	 * @return Die aktualisierte Ausleihe oder ein 404-Fehler, wenn sie nicht existiert
	 */
	@PutMapping("/extend/{id}")
	public ResponseEntity<Borrowing> extendBorrowing(@PathVariable Long id) {
		if (!this.borrowingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
		
        Optional<Borrowing> borrowingOpt = this.borrowingRepository.findById(id);
        Borrowing borrowing = borrowingOpt.get();
        if (borrowing.getDateReturned() != null) {
            return ResponseEntity.badRequest().build();
    	}
		
        borrowing.extend();
        Borrowing updatedBorrowing = this.borrowingRepository.save(borrowing);
        return ResponseEntity.ok(updatedBorrowing);
	}
	
	/**
     * Löscht eine Ausleihe anhand ihrer ID
     * 
     * @param id - Die ID der zu löschenden Ausleihe
     * @return Ein 204-Status bei Erfolg, oder ein 404-Fehler, wenn sie nicht existiert
     */
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> deleteBorrowing(@PathVariable Long id) {
		if (!this.borrowingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
		
        this.borrowingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
	}
	
	/**
	 * Gibt ein Medium zurück und löscht die Ausleihe
	 * 
	 * @param mediaid - Die ID des zurückgegebenen Mediums
     * @return Ein 204-Status bei Erfolg, oder ein 404-Fehler, wenn sie nicht existiert
	 */
	@DeleteMapping("/return/{mediaid}")
	@Transactional
	public ResponseEntity<Void> returnMedia(@PathVariable Long mediaid) {
		Optional<Borrowing> borrowing = this.borrowingRepository.findActiveByMediaId(mediaid);
        if (borrowing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        this.borrowingRepository.delete(borrowing.get());
        return ResponseEntity.noContent().build();
	}
}