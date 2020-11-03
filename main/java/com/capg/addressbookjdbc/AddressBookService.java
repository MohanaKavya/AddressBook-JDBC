/**
 * 
 */
package com.capg.addressbookjdbc;

import java.io.IOException;
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

}
