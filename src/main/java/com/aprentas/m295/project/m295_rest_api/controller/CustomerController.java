package com.aprentas.m295.project.m295_rest_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aprentas.m295.project.m295_rest_api.model.Address;
import com.aprentas.m295.project.m295_rest_api.model.Customer;
import com.aprentas.m295.project.m295_rest_api.repository.AddressRepository;
import com.aprentas.m295.project.m295_rest_api.repository.CustomerRepository;

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
@RequestMapping("/api/customers")
@CrossOrigin("**")
public class CustomerController {

    private final CustomerRepository customerRepository;
	
	private final AddressRepository addressRepository;
    
	@Autowired
	public CustomerController(CustomerRepository customerRepository, AddressRepository addressRepository) {
		super();
		this.customerRepository = customerRepository;
		this.addressRepository = addressRepository;
	}
	
	/**
	 * Gibt einen Kunden anhand seiner ID zurück
	 * 
	 * @param id - Die ID des gesuchten Kunden
	 * @return Der Kunde mit der zugehörigen ID oder ein 404-Fehler, wenn die ID nicht existiert
	 */
    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<Customer>> getCustomerById(@PathVariable Long id) {
    	if (!this.customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
    	
    	Optional<Customer> customer = this.customerRepository.findById(id);
    	return ResponseEntity.ok(customer);
    }

	/**
     * Gibt Kunden anhand ihres Nachnamens zurück
     * 
     * @param lastname - Der Nachname des gesuchten Kunden
     * @return Eine Liste von Kunden mit dem zugehörigen Nachnamen
     */
    @GetMapping("/lastname/{lastname}")
    public ResponseEntity<List<Customer>> getCustomerByLastName(@PathVariable String lastname) {
    	List<Customer> customers = this.customerRepository.findByLastName(lastname);
    	return ResponseEntity.ok(customers);
    }
    
    /**
     * Gibt Kunden anhand ihrer Adresse zurück
     * 
     * @param addressid - Die ID der Adresse des gesuchten Kunden
     * @return Eine Liste von Kunden mit der zugehörigen Adresse
     */
    @GetMapping("/{addressid}")
    public ResponseEntity<List<Customer>> getCustomerByAddress(@PathVariable Long addressid) {
    	if (!this.addressRepository.existsById(addressid)) {
            return ResponseEntity.notFound().build();
        }
    	
    	List<Customer> customers = this.customerRepository.findByAddress(addressid);
    	return ResponseEntity.ok(customers);
    }
    
    /**
     * Erstellt einen neuen Kunden
     * 
     * @param customer - Die Daten des neuen Kunden
     * @return Der erstellte Kunde
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
    	if (customer.getAddress() == null || customer.getAddress().getId() == null ||
    		!this.addressRepository.existsById(customer.getAddress().getId())) {
            return ResponseEntity.badRequest().build();
        }
    	
    	Optional<Address> address = this.addressRepository.findById(customer.getAddress().getId());
        customer.setAddress(address.get());
    	
    	Customer newCustomer = this.customerRepository.save(customer);
    	return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
    }
    
    /**
     * Aktualisiert einen bestehenden Kunden
     * 
     * @param id - Die ID des zu aktualisierenden Kunden
     * @param address - Die Daten des aktualisierten Kunden
     * @return - Der aktualisierte Kunde oder ein 404-Fehler, wenn er nicht existiert
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
    	 if (!this.customerRepository.existsById(id)) {
             return ResponseEntity.notFound().build();
         }
    	 
    	 customer.setId(id);
         Customer updatedCustomer = this.customerRepository.save(customer);
         return ResponseEntity.ok(updatedCustomer);
    }
    

    /**
     * Löscht einen Kunden anhand seiner ID
     * 
     * @param id - Die ID des zu löschenden Kunden
     * @return Ein 204-Status bei Erfolg, oder ein 404-Fehler, wenn er nicht existiert
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    	if (!this.customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
    	
        this.customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}