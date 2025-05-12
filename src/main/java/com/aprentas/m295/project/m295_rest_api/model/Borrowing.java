package com.aprentas.m295.project.m295_rest_api.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Entity/DTO-Klasse für die Ausleih-Ressource.
 * Repräsentiert eine Medienausleihe durch einen Kunden.
 * 
 * @author Luca Minotti
 * @version 1.4
 * @since 1.0
 */
@Entity
@Table(name = "borrowing", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"mediaid"}, name = "unique_active_media")
})
public class Borrowing {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateborrowed")
    private LocalDate dateBorrowed;

    @Column(name = "datereturned")
    private LocalDate dateReturned;

    @Column(name = "duedate", nullable = false)
    private LocalDate dueDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mediaid", nullable = false)
    private Media media;

	public Borrowing() {
		super();
		this.dateBorrowed = LocalDate.now();
		this.dueDate = LocalDate.now().plusDays(14);
	}
	
	public Borrowing(Customer customer, Media media) {
		super();
		this.customer = customer;
		this.media = media;
	}

	public Borrowing(LocalDate dateBorrowed, LocalDate dateReturned, LocalDate dueDate, Customer customer,
			Media media) {
		super();
		this.dateBorrowed = dateBorrowed;
		this.dateReturned = dateReturned;
		this.dueDate = dueDate;
		this.customer = customer;
		this.media = media;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateBorrowed() {
		return dateBorrowed;
	}

	public void setDateBorrowed(LocalDate dateBorrowed) {
		this.dateBorrowed = dateBorrowed;
	}

	public LocalDate getDateReturned() {
		return dateReturned;
	}

	public void setDateReturned(LocalDate dateReturned) {
		this.dateReturned = dateReturned;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	/**
     * Verlängert die Ausleihe, indem das Ausleih- und Fälligkeitsdatum aktualisiert wird.
     */
    public void extend() {
        this.dateBorrowed = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(14);
    }

	@Override
	public int hashCode() {
		return Objects.hash(customer, dateBorrowed, dateReturned, dueDate, id, media);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Borrowing other = (Borrowing) obj;
		return Objects.equals(customer, other.customer) && Objects.equals(dateBorrowed, other.dateBorrowed)
				&& Objects.equals(dateReturned, other.dateReturned) && Objects.equals(dueDate, other.dueDate)
				&& Objects.equals(id, other.id) && Objects.equals(media, other.media);
	}

	@Override
	public String toString() {
		return "Borrowing [id=" + id + ", dateBorrowed=" + dateBorrowed + ", dateReturned=" + dateReturned
				+ ", dueDate=" + dueDate + ", customer=" + customer + ", media=" + media + "]";
	}
}