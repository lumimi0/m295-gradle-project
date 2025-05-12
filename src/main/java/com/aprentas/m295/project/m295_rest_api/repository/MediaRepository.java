package com.aprentas.m295.project.m295_rest_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aprentas.m295.project.m295_rest_api.model.Media;

/**
 * Repository-Interface für die Interaktion mit der media-Tabelle in der Datenbank.
 * Erweitert das JpaRepository für grundlegende CRUD-Operationen.
 * 
 * @author Luca Minotti
 * @version 1.1
 * @since 1.0
 */
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
	/**
	 * Findet alle Medien
	 * 
	 * @return Eine Liste mit allen Medien
	 */
	@Override
    List<Media> findAll();
    
    /**
	 * Findet ein Medium anhand der ID
	 *  
	 * @param id - Die zu suchende ID
	 * @return Ein Medium, das der ID entspricht
	 */
	@Override
	@Query("select m from Media where m.id = :id")
    Optional<Media> findById(@Param("id") Long id);
    
    /**
	 * Findet Medien anhand des Titels
	 *  
	 * @param title - Der zu suchende Titel
	 * @return Eine Liste von Medien, die dem Titel entsprechen
	 */
	@Query("select m from Media where m.title = :title")
    List<Media> findByTitle(@Param("title") String title);
}