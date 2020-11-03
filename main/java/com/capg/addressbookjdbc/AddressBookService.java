/**
 * 
 */
package com.capg.addressbookjdbc;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mohana Kavya
 *
 */
public class AddressBookService {
	private static Logger log = Logger.getLogger(AddressBookService.class.getName());
	
	private List<Contact> contactList;
	private AddressBookDBService addressBookDBService;

	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
	}

	public AddressBookService(List<Contact> contactList) {
		this();
		this.contactList = contactList;
	}

	public static void main(String[] args) {
		
		Handler fileHandler = null;
		try {
			fileHandler = new FileHandler("C:\\Users\\Mohana Kavya\\eclipse-workspace\\AddressBookJDBC\\src\\main\\resources\\Address Book JDBC.log");
			fileHandler.setLevel(Level.ALL);
			log.addHandler(fileHandler);
		} catch (SecurityException | IOException e) {
			log.log(Level.SEVERE, "Failed to find the output Stream File", e);
		}

		// Welcome message added
		log.log(Level.INFO, "Welcome to Address Book Database Connectivity");
	}

	/**
	 * @return List of Person Contact Details
	 */
	public List<Contact> readContactData() {
		try {
			this.contactList = addressBookDBService.readData();
			if(!(contactList.isEmpty()))
				return contactList;
			else
				throw new AddressBookJDBCException("Result Set is Empty", AddressBookJDBCException.ExceptionType.DATA_RETRIEVAL_EXCEPTION);
			
		} catch (AddressBookJDBCException e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		return contactList;
		
	}

	/**
	 * @param email : Unique identification 
	 * @param address to be updates
	 */
	public void updateContactDetails(String email, String address) {
		try {
			int numOfRowsModified = addressBookDBService.updateEmployeeData(email, address);
			if (numOfRowsModified == 0) 
				throw new AddressBookJDBCException("no rows updated", AddressBookJDBCException.ExceptionType.UPDATE_DATABASE_EXCEPTION);
			Contact contact = this.getContactData(email);
			if (contact != null)
				contact.address = address;
		} catch(AddressBookJDBCException e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		
	}

	/**
	 * @param email : unique identification
	 * @return Contact Class object
	 */
	private Contact getContactData(String email) {
		return this.contactList.stream().filter(contact -> contact.email.equals(email)).findFirst().orElse(null);
	}

	/**
	 * @param email : unique identification
	 * @return true or false
	 */
	public boolean checkConatctDetailsInSyncWithDB(String email) {
		List<Contact> contactList = addressBookDBService.getContactDataByEmail(email);
		return contactList.get(0).equals(getContactData(email));
	}

}
