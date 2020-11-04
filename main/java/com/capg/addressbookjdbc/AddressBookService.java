/**
 * 
 */
package com.capg.addressbookjdbc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mohana Kavya
 *
 */
public class AddressBookService {
	public enum IOService {
		DB_IO
	}

	private static Logger log = Logger.getLogger(AddressBookService.class.getName());
	
	private List<Contact> contactList;
	private AddressBookDBService addressBookDBService;
	private Map<String, Integer> contactByCityOrState;

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

	/**
	 * @param startDate
	 * @param endDate
	 * @return List of Person Contact Details
	 */
	public List<Contact> readContactDataForGivenDateRange(LocalDate startDate, LocalDate endDate) {
		try {
			List<Contact> contactList = addressBookDBService.getContactForGivenDateRange(startDate, endDate);

			if(!(contactList.isEmpty()))
				return contactList;
			else
				throw new AddressBookJDBCException("no rows selected for the given date range", AddressBookJDBCException.ExceptionType.DATA_RETRIEVAL_EXCEPTION);
			} catch(AddressBookJDBCException e) {
				log.log(Level.SEVERE, e.getMessage()+" : "+e);
			}
		return null;
	}

	/**
	 * @return Map Key : City/State Names Values : Count of Persons 
	 */
	public Map<String, Integer> readContactByCityOrState() {
		this.contactByCityOrState=addressBookDBService.getContactsByCityOrState();
		return contactByCityOrState;
	}

	/**
	 *Insert new record into Database
	 */
	public void addContactToDatabase(String firstName, String lastName, String address, String city, String state,
			int zip, String phoneNo, String email, String addressBookName, String type, LocalDate date) {
		contactList.add(addressBookDBService.addContact(firstName, lastName, address, city, state, zip, phoneNo, email,
				addressBookName, date));		
	}
	public long countEntries(IOService ioService) {
		return contactList.size();
	}

	public List<Contact> readData(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			this.contactList = addressBookDBService.readData();
		return contactList;
	}
	public void addContactToDB(String firstName, String lastName, String address, String city, String state, int zip,
			String phone, String email, String addressBookName, LocalDate startDate) {
		contactList.add(addressBookDBService.addContact(firstName, lastName, address, city, state, zip, phone, email,
				addressBookName, startDate));

	}

	public void addContact(List<Contact> contactDataList) {
		contactDataList.forEach(contactData -> {
			log.info("Employee being added : " + contactData.firstName);
			this.addContactToDB(contactData.firstName, contactData.lastName, contactData.address, contactData.city,
					contactData.state, contactData.zip, contactData.phoneNumber, contactData.email,
					contactData.addressBookName, contactData.date);
			log.info("Employee added : " + contactData.firstName);
		});
		log.info("" + this.contactList);
	}

	public void addEmployeeToPayrollWithThreads(List<Contact> contactDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
		contactDataList.forEach(contactData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(contactData.hashCode(), false);
				log.info("Employee being added : " + Thread.currentThread().getName());
				this.addContactToDB(contactData.firstName, contactData.lastName, contactData.address, contactData.city,
						contactData.state, contactData.zip, contactData.phoneNumber, contactData.email,
						contactData.addressBookName, contactData.date);
				employeeAdditionStatus.put(contactData.hashCode(), true);
				log.info("Employee added : " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, contactData.firstName);
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		log.info("" + this.contactList);
	}

}
