package com.learn2gether.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Customer{

	public Customer(){}
	
	public Customer(String firstName, String lastName, Date dateOfBirth,CustomerImage customerImage, Address address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.customerImage = customerImage;
		this.address = address;
	}

	@Id
	@Getter
    @GeneratedValue
    private long id;
	
	@Setter
	@Getter
	@Column(nullable = false, length = 30)
	private String firstName;
	
	@Setter
	@Getter
	@Column(nullable = false, length = 30)
	private String lastName;
	
	@Setter	
	@Getter
	@Column(nullable = false)
	private Date dateOfBirth;
	
	@Setter
	@Getter
	@OneToOne(cascade = {CascadeType.ALL})
	private CustomerImage customerImage;
	
	@Setter
	@Getter
	@OneToOne(cascade = {CascadeType.ALL})
	private Address address;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public CustomerImage getCustomerImage() {
		return customerImage;
	}

	public void setCustomerImage(CustomerImage customerImage) {
		this.customerImage = customerImage;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}