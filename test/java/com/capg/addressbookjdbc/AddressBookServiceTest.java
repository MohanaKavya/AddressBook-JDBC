/**
 * 
 */
package com.capg.addressbookjdbc;

import java.util.List;

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
}
