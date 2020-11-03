/**
 * 
 */
package com.capg.addressbookjdbc;

import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mohana Kavya
 *
 */
public class AddressBookDBService {
	private static Logger log = Logger.getLogger(AddressBookDBService.class.getName());
	private static AddressBookDBService addressBookDBService;
	private PreparedStatement preparedStatement;

	private AddressBookDBService() {
	}

	static AddressBookDBService getInstance() {
		if (addressBookDBService == null) {
			addressBookDBService = new AddressBookDBService();
			Handler fileHandler = null;
			try {
				fileHandler = new FileHandler("C:\\Users\\Mohana Kavya\\eclipse-workspace\\AddressBookJDBC\\src\\main\\resources\\Address Book DB Service.log");
				fileHandler.setLevel(Level.ALL);
				log.addHandler(fileHandler);
			} catch (SecurityException | IOException e) {
				log.log(Level.SEVERE, "Failed to find the output Stream File", e);
			}
		}
		return addressBookDBService;
	}

	public Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String userName = "root";
		String password = "L3al!lhope";
		Connection connection;
		log.info("Connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		log.info("Connection successful: " + connection);
		return connection;
	}

	public List<Contact> readData() {
		String sql = "select a.address_book_name, a.address_book_type, c.first_name, c.last_name, c.email_id, p.phone_no, d.address, d.city, d.state, d.zip"
				+ " from contact c"
				+ " inner join address_book_dictionary a"
				+ " on c.address_book_id = a.address_book_id"
				+ " inner join contact_number p"
				+ " on c.email_id = p.email_id"
				+ " inner join contact_address d"
				+ " on c.email_id = d.email_id;";
		return this.getContactDetailsUsingSqlQuery(sql);
	}

	public int updateEmployeeData(String email, String address) {
		try (Connection connection = this.getConnection();) {
			String sql = "update contact_address set address=? where email_id=?;";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, address);
			preparedStatement.setString(2, email);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Failed : "+e);
		}
		return 0;
	}

	public List<Contact> getContactForGivenDateRange(LocalDate startDate, LocalDate endDate) {
		String sql = String.format("select a.address_book_name, a.address_book_type, c.first_name, c.last_name, c.email_id, c.date_added, p.phone_no, d.address, d.city, d.state, d.zip"
				+ " from contact c"
				+ " inner join address_book_dictionary a"
				+ " on c.address_book_id = a.address_book_id AND c.date_added BETWEEN '%s' and '%s'"
				+ " inner join contact_number p"
				+ " on c.email_id = p.email_id"
				+ " inner join contact_address d"
				+ " on c.email_id = d.email_id;", Date.valueOf(startDate), Date.valueOf(endDate));
		return this.getContactDetailsUsingSqlQuery(sql);
	}

	public List<Contact> getContactDataByEmail(String email) {
		String sql = String.format("select a.address_book_name, a.address_book_type, c.first_name, c.last_name, c.email_id, p.phone_no, d.address, d.city, d.state, d.zip"
					+ " from contact c"
					+ " inner join address_book_dictionary a"
					+ " on c.address_book_id = a.address_book_id"
					+ " inner join contact_number p"
					+ " on c.email_id = p.email_id"
					+ " inner join contact_address d"
					+ " on c.email_id = d.email_id where d.email_id = '%s';", email);
		return this.getContactDetailsUsingSqlQuery(sql);
	}
	
	public Map<String, Integer> getContactsByCityOrState() {
		Map<String, Integer> contactByCityOrStateMap = new HashMap<>();
		ResultSet resultSet;
		String sqlCity = "SELECT city, count(email_id) as count_by_city from contact_address group by City; ";
		String sqlState = "SELECT state, count(email_id) as count_by_state from contact_address group by State;";
		try (Connection connection = addressBookDBService.getConnection()) {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlCity);
			while (resultSet.next()) {
				String city = resultSet.getString("city");
				Integer count = resultSet.getInt("count_by_city");
				contactByCityOrStateMap.put(city,count);
			}
			resultSet = statement.executeQuery(sqlState);
			while (resultSet.next()) {
				String state = resultSet.getString("state");
				Integer count = resultSet.getInt("count_by_state");
				contactByCityOrStateMap.put(state,count);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Failed : "+e);
		}
		return contactByCityOrStateMap;
	}

	private List<Contact> getContactDetailsUsingSqlQuery(String sql) {
		List<Contact> ContactList = null;
		try (Connection connection = this.getConnection();) {
			this.preparedStatement = connection.prepareStatement(sql);
			ResultSet result = this.preparedStatement.executeQuery(sql);
			ContactList = this.getAddressBookData(result);
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Failed : "+e);
		}
		return ContactList;
	}

	private List<Contact> getAddressBookData(ResultSet result) {
		List<Contact> contactList = new ArrayList<>();
		try {
			while (result.next()) {
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String addressBookName = result.getString("address_book_name");
				String address = result.getString("address");
				String city = result.getString("city");
				String state = result.getString("state");
				int zip = result.getInt("zip");
				String phoneNumber = result.getString("phone_no");
				String email = result.getString("email_id");
				String addressBookType = result.getString("address_book_type");
				contactList.add(new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email,
						addressBookName, addressBookType));
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Failed : "+e);
		}
		return contactList;
	}

}
