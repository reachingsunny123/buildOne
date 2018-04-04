package com.learn2gether.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Address{

	public Address(){}
	
	public Address(String street, String town, String county, String postCode) {
		this.street = street;
		this.town = town;
		this.county = county;
		this.postcode = postCode;
	}

	@Id
	@Getter
    @GeneratedValue
    private long id;
	
	@Setter
	@Getter
	@Column(name = "street", nullable = false, length=40)
	private String street;
	
	@Setter
	@Getter
	@Column(name = "town", nullable = false, length=40)
	private String town;
	
	@Setter 
	@Getter
	@Column(name = "county", nullable = false, length=40)
	private String county;

	@Setter
	@Getter
	@Column(name = "postcode", nullable = false, length=40)
	private String postcode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	
}