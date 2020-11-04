/**
 * 
 */
package com.capg.addressbookjdbc;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Mohana Kavya
 *
 */
public class Contact {
	public String firstName;
	public String lastName;
	public String address;
	public String city;
	public String state;
	public int zip;
	public String phoneNumber;
	public String email;
	public String addressBookName;
	public String addressBookType;
	public LocalDate date;

	public Contact(String firstName, String lastName, String address, String city, String state, int zip,
			String phoneNumber, String email, String addressBookName, String addressBookType) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.addressBookName = addressBookName;
		this.addressBookType = addressBookType;
	}
	
	public Contact(String firstName, String lastName, String address, String city, String state, int zip,
			String phoneNumber, String email, String addressBookName, String addressBookType, LocalDate date) {
		this(firstName, lastName, address, city, state, zip, phoneNumber, email, addressBookName, addressBookType);
		this.date = date;
	}
	
	@Override
	public String toString() {
	return "First Name: " + this.firstName + " Last Name: " + this.lastName + " Address: " + this.address
			+ " City: " + this.city + " State: " + this.state + " Zip: " + this.zip + " Phone Number: "
			+ this.phoneNumber + " Email: " + this.email + " Address book name" + this.addressBookName + " type"
			+ addressBookType;

	}
	public Contact(String firstName, String lastName, String address, String city, String state, int zip,
			String phoneNumber, String email, String addressBookName, LocalDate startDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.addressBookName = addressBookName;
		this.date = startDate;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Contact that = (Contact) o;
		return firstName.equals(that.firstName) && address.equals(that.address) && email.equals(that.email);
	}
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, address, city, state, zip, phoneNumber, email, addressBookName,
				date);
	}
}
