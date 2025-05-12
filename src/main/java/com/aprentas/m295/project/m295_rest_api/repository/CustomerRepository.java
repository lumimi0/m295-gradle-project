package com.aprentas.m295.project.m295_rest_api.repository;

import com.aprentas.m295.project.m295_rest_api.model.Customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository-Interface für die Interaktion mit der customer-Tabelle in der Datenbank.
 * Erweitert das JpaRepository für grundlegende CRUD-Operationen.
 * 
 * @author Luca Minotti
 * @version 1.1
 * @since 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	/**
	 * Findet einen Kunden anhand der ID
	 *  
	 * @param id - Die zu suchende ID
	 * @return Ein Kunde, der der ID entspricht
	 */
	@Override
	Optional<Customer> findById(Long id);
	
	/**
	 * Findet Kunden anhand des Nachnamens
	 *  
	 * @param lastName - Der zu suchende Nachname
	 * @return Eine Liste von Kunden, die dem Nachnamen entsprechen
	 */
	@Query("select c from Customer where c.lastname = :lastName")
	List<Customer> findByLastName(@Param("lastName") String lastName);
	
	/**
	 * Findet Kunden anhand der Adresse
	 *  
	 * @param addressId - Die ID der zu suchenden Adresse
	 * @return Eine Liste von Kunden, die der Adresse entsprechen
	 */
	@Query("select c from Customer where c.addressid = :addressId")
	List<Customer> findByAddress(@Param("addressId") Long addressId);
}