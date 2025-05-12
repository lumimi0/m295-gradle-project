package com.aprentas.m295.project.m295_rest_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aprentas.m295.project.m295_rest_api.model.Address;
import com.aprentas.m295.project.m295_rest_api.repository.AddressRepository;

import jakarta.transaction.Transactional;

/**
 * REST-Controller für Adresse-Operationen
 * Stellt Endpunkte zum Verwalten von Adressen bereit
 * 
 * @author Luca Minotti
 * @version 1.3
 * @since 1.0
 */
@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins= "*")
public class AddressController {

	private final AddressRepository addressRepository;
	
	@Autowired
	public AddressController(AddressRepository addressRepository) {
		super();
		this.addressRepository = addressRepository;
	}

	/**
	 * Gibt alle Adressen zurück
	 * 
	 * @return Eine Liste aller Adressen
	 */
    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
    	List<Address> addresses = this.addressRepository.findAll();
		return ResponseEntity.ok(addresses);
    }
    
	/**
     * Gibt Adressen anhand ihrer Strasse und Hausnummer zurück
     * 
     * @param street - Die Strasse der gesuchten Adresse
     * @param streetnumber - Die Hausnummer der gesuchten Adresse
     * @return Eine Liste aller Adressen mit den zugehörigen Strassen und Hausnummern
     */
    @GetMapping("/street/{street}/streetnumber/{streetnumber}")
    public ResponseEntity<List<Address>> getAddressByStreet(
    		@PathVariable String street, 
    		@PathVariable String streetnumber
    ) {
    	List<Address> addresses = this.addressRepository.findByStreet(street, streetnumber);
    	return ResponseEntity.ok(addresses);
    }
    
    /**
     * Gibt Adressen anhand der PLZ zurück
     * 
     * @param zip - Die PLZ der gesuchten Adresse
     * @return Eine Liste aller Adressen mit der zugehörigen PLZ
     */
    @GetMapping("/zip/{zip}")
    public ResponseEntity<List<Address>> getAddressByZip(@PathVariable String zip) {
    	List<Address> addresses = this.addressRepository.findByZip(zip);
    	return ResponseEntity.ok(addresses);
    }
    
    /**
     * Erstellt eine neue Adresse
     * 
     * @param address - Die Daten der neuen Adresse
     * @return Die erstellte Adresse
     */
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
    	Address newAddress = this.addressRepository.save(address);
    	return ResponseEntity.status(HttpStatus.CREATED).body(newAddress);
    }
    
    /**
     * Aktualisiert eine bestehende Adresse
     * 
     * @param id - Die ID der zu aktualisierenden Adresse
     * @param address - Die Daten der aktualisierten Adresse
     * @return - Die aktualisierte Adresse oder ein 404-Fehler, wenn sie nicht existiert
     */
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address address) {
    	if (!this.addressRepository.existsById(id)) {
    		return ResponseEntity.notFound().build();
    	}
    	
    	address.setId(id);
    	Address updatedAddress = this.addressRepository.save(address);
    	return ResponseEntity.ok(updatedAddress);
    }
    
    /**
     * Löscht eine Adresse anhand ihrer ID
     * 
     * @param id - Die ID der zu löschenden Adresse
     * @return Ein 204-Status bei Erfolg, oder ein 404-Fehler, wenn sie nicht existiert oder ein 400-Fehler, wenn sie referenziert ist
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
    	if (!this.addressRepository.existsById(id)) {
    		return ResponseEntity.notFound().build();
    	}
    	
    	if (this.addressRepository.isAddressReferenced(id)) {
            return ResponseEntity.badRequest().build();
        }
    	
    	this.addressRepository.deleteById(id);
    	return ResponseEntity.noContent().build();
    }
}
