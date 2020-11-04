/**
 * 
 */
package com.capg.addressbookjdbc;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.capg.addressbookjdbc.AddressBookService.IOService;

/**
 * @author Mohana Kavya
 *
 */
public class AddressBookServiceTest {
	private static Logger log = Logger.getLogger(AddressBookService.class.getName());

	@Test
	public void contactsWhenRetrievedFromDB_ShouldMatchCount() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readContactData();
		Assert.assertEquals(5, contactList.size());
	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readContactData();
		addressBookService.updateContactDetails("lakshmi@outlook.com", "Palem");
		boolean result = addressBookService.checkConatctDetailsInSyncWithDB("lakshmi@outlook.com");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readContactData();
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.of(2019, 12, 30);
		List<Contact> contactList = addressBookService.readContactDataForGivenDateRange(startDate, endDate);
		Assert.assertEquals(2, contactList.size());
	}
	
	@Test
	public void givenContacts_RetrieveNumberOfContacts_ByCityOrState() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readContactData();
		Map<String, Integer> contactByCityOrStateMap = addressBookService.readContactByCityOrState();
		Assert.assertEquals(true, contactByCityOrStateMap.get("Hyd").equals(1));
		Assert.assertEquals(true, contactByCityOrStateMap.get("Kerala").equals(1));
	}
	
	@Test
	public void givenNewContact_WhenAdded_ShouldSyncWithDB() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readContactData();
		LocalDate date = LocalDate.of(2020, 9, 23);
		addressBookService.addContactToDatabase("Arjun", "Ganesh", "Whitefield", "Bangalore", "Karnataka", 700012, "9898989898",
				"arjun@gmail.com", "College", "Alumni", date);
		boolean result = addressBookService.checkConatctDetailsInSyncWithDB("arjun@gmail.com");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenContacts_WhenAddedToDB_ShouldMatchEmployeeEntries() {
		Contact[] arrayOfEmployee = {
				new Contact("Mohana", "Kavya", "Sathupalli", "Khammam", "Telangana", 507012, "98654331",
						"mohanakavya@gmail.com", "Casual", LocalDate.now()),
				new Contact("Mounika", "Anne", "Miyapur", "Hyderabad", "Telangana", 500050, "96763129",
						"mounikaanne@gmail.com", "Personal", LocalDate.now()),
				new Contact("Sohail", "Syed", "SGNagar", "Kalpakkam", "TamilNadu", 600010, "87655433",
						"sohailsyed@gmail.com", "Corporate", LocalDate.now()) };
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readData(IOService.DB_IO);
		Instant start = Instant.now();
		addressBookService.addContact(Arrays.asList(arrayOfEmployee));
		Instant end = Instant.now();
		log.info("Duration without thread : " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		addressBookService.addEmployeeToPayrollWithThreads(Arrays.asList(arrayOfEmployee));
		Instant threadEnd = Instant.now();
		log.info("Duartion with Thread : " + Duration.between(threadStart, threadEnd));
		Assert.assertEquals(10, addressBookService.countEntries(IOService.DB_IO));
	}
}
