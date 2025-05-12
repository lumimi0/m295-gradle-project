package com.aprentas.m295.project.m295_rest_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aprentas.m295.project.m295_rest_api.model.Address;

/**
 * Repository-Interface für die Interaktion mit der address-Tabelle in der Datenbank.
 * Erweitert das JpaRepository für grundlegende CRUD-Operationen.
 * 
 * @author Luca Minotti
 * @version 1.2
 * @since 1.0
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
	/**
	 * Findet alle Adressen
	 * 
	 * @return Eine Liste mit allen Adressen
	 */
	@Override
	List<Address> findAll();
	
	/**
	 * Findet Adressen anhand der Strasse und Hausnummer
	 * 
	 * @param street - Die zu suchende Strasse
	 * @param streetNumber - Die zu suchende Hausnummer
	 * @return Eine Liste von Adressen, die der Strasse und Hausnummer entsprechen
	 */
	@Query("select a from Address a where a.street = :street and a.streetNumber = :streetNumber")
	List<Address> findByStreet(@Param("street") String street, @Param("streetNumber") String streetNumber);
	
	/**
	 * Findet Adressen anhand der PLZ
	 * 
	 * @param zip - Die zu suchende PLZ
	 * @return Eine Liste von Adressen, die der PLZ entsprechen
	 */
	@Query("select a from Address where a.zip = :zip")
	List<Address> findByZip(@Param("zip") String zip);
	
	/**
     * Prüft, ob eine Adresse von Kunden referenziert wird
     *
     * @param addressId - Die zu prüfende Adress-ID
     * @return true, wenn die Adresse referenziert wird, sonst false
     */
   @Query(value = "select case when count(c) > 0 then true else false end from Customer c where c.addressid = :addressId", nativeQuery = true)
   boolean isAddressReferenced(@Param("addressId") Long addressId);
}