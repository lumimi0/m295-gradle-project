package com.aprentas.m295.project.m295_rest_api.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Entity/DTO-Klasse für die Kunden-Ressource.
 * Repräsentiert einen Bibliotheksnutzer.
 * 
 * @author Luca Minotti
 * @version 1.2
 * @since 1.0
 */
@Entity
@Table(name = "customer")
public class Customer {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false, length = 40)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 40)
    private String lastName;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressid", nullable = false)
    private Address address;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

	public Customer() {
		super();
	}

	public Customer(String firstName, String lastName, LocalDate birthDate, Address address, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.address = address;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, birthDate, email, firstName, id, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(address, other.address) && Objects.equals(birthDate, other.birthDate)
				&& Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate
				+ ", address=" + address + ", email=" + email + "]";
	}
}