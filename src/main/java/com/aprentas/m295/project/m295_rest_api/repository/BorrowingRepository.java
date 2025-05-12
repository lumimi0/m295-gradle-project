package com.aprentas.m295.project.m295_rest_api.repository;

import com.aprentas.m295.project.m295_rest_api.model.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Borrowing entity operations
 * 
 * @author Luca Minotti
 * @version 1.2
 * @since 1.0
 */
@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    
	/**
	 * Findet alle Ausleihen
	 * 
	 * @return Eine Liste mit allen Ausleihen
	 */
	@Override
	List<Borrowing> findAll();
	
	/**
	 * Findet eine Ausleihe anhand der ID
	 * 
	 * @param id - Die zu suchende ID
	 * @return Eine Ausleihe, die der ID entspricht
	 */
	@Override
	@Query("select b from Borrowing where b.id = :id")
	Optional<Borrowing> findById(@Param("id") Long id);
	
	/**
	 * Findet eine aktive Ausleihe anhand der Medien-ID
	 * 
	 * @param mediaId - Die ID des Mediums
	 * @return Eine Ausleihe mit der zugehörigen Medien-ID
	 */
	Optional<Borrowing> findActiveByMediaId(@Param("mediaId") Long mediaId);
	
	/**
	 * Prüft, ob ein Medium ausgeliehen ist
	 * 
	 * @param mediaId - Die ID des Mediums
	 * @return true, wenn das Medium ausgeliehen ist, false wenn nicht
	 */
	boolean isMediaBorrowed(@Param("mediaId") Long mediaId);
	
	/**
	 * Löscht eine Ausleihe anhand der Medien-ID
	 * 
	 * @param mediaId - Die ID des Mediums
	 */
	void deleteByMediaId(@Param("mediaId") Long mediaId);
}