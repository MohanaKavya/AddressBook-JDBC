/**
 * 
 */
package com.capg.addressbookjdbc;

/**
 * @author Mohana Kavya
 *
 */
public class AddressBookJDBCException extends Exception {
	enum ExceptionType {
		DATA_RETRIEVAL_EXCEPTION
	}

	ExceptionType type;

	public AddressBookJDBCException(String message, ExceptionType type) {
		super(message);
		this.type = type;
	}

}
