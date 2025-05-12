package com.aprentas.m295.project.m295_rest_api.model;

import java.util.Objects;

import jakarta.persistence.*;

/**
 * Entity/DTO-Klasse für die Medien-Ressource.
 * Repräsentiert Bücher, Filme und andere Medien in der Bibliothek.
 *
 * @author Luca Minotti
 * @version 1.1
 * @since 1.0
 */
@Entity
@Table(name = "media")
public class Media {
   
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column(name = "title", nullable = false, length = 50)
	private String title;
	
	@Column(name = "author", nullable = false, length = 80)
	private String author;
	
	@Column(name = "genre", length = 20)
	private String genre;
	
	@Column(name = "rating")
	private Integer rating;
	
	@Column(name = "isbnorean", length = 13)
	private Long isbnOrEan;
	
	@Column(name = "fsk")
	private Integer fsk;
	
	@Column(name = "shelfcode", length = 20)
	private String shelfCode;

	public Media() {
		super();
	}
	
	public Media(Long id, String title, String author, String genre, Integer rating, Long isbnOrEan, Integer fsk,
			String shelfCode) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.rating = rating;
		this.isbnOrEan = isbnOrEan;
		this.fsk = fsk;
		this.shelfCode = shelfCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getIsbnOrEan() {
		return isbnOrEan;
	}

	public void setIsbnOrEan(Long isbnOrEan) {
		this.isbnOrEan = isbnOrEan;
	}

	public Integer getFsk() {
		return fsk;
	}

	public void setFsk(Integer fsk) {
		this.fsk = fsk;
	}

	public String getShelfCode() {
		return shelfCode;
	}

	public void setShelfCode(String shelfCode) {
		this.shelfCode = shelfCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, fsk, genre, id, isbnOrEan, rating, shelfCode, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Media other = (Media) obj;
		return Objects.equals(author, other.author) && Objects.equals(fsk, other.fsk)
				&& Objects.equals(genre, other.genre) && Objects.equals(id, other.id)
				&& Objects.equals(isbnOrEan, other.isbnOrEan) && Objects.equals(rating, other.rating)
				&& Objects.equals(shelfCode, other.shelfCode) && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Media [id=" + id + ", title=" + title + ", author=" + author + ", genre=" + genre + ", rating=" + rating
				+ ", isbnOrEan=" + isbnOrEan + ", fsk=" + fsk + ", shelfCode=" + shelfCode + "]";
	}
}