/**
 * 
 */
package com.capg.addressbookjdbc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mohana Kavya
 *
 */
public class AddressBookServiceTest {
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
}
