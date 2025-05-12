package com.aprentas.m295.project.m295_rest_api.model;

import java.util.Objects;

import jakarta.persistence.*;

/**
 * Entity/DTO-Klasse für die Adress-Ressource.
 * Repräsentiert die Adressinformationen der Kunden.
 * 
 * @author Luca Minotti
 * @version 1.1
 * @since 1.0
 */
@Entity
@Table(name = "address")
public class Address {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street", nullable = false, length = 40)
    private String street;

    @Column(name = "streetnumber", nullable = false, length = 40)
    private String streetNumber;

    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Column(name = "zip", nullable = false, length = 8)
    private String zip;

	public Address() {
		super();
	}

	public Address(String street, String streetNumber, String city, String zip) {
		super();
		this.street = street;
		this.streetNumber = streetNumber;
		this.city = city;
		this.zip = zip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, id, street, streetNumber, zip);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city) && Objects.equals(id, other.id) && Objects.equals(street, other.street)
				&& Objects.equals(streetNumber, other.streetNumber) && Objects.equals(zip, other.zip);
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", streetNumber=" + streetNumber + ", city=" + city
				+ ", zip=" + zip + "]";
	}
}