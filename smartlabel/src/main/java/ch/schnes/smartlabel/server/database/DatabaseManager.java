package ch.schnes.smartlabel.server.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import ch.schnes.smartlabel.server.Logger;

public class DatabaseManager {
	private final Logger logger = Logger.getInstance();
	private final String INSTANCE = "DatabaseManager";
	private Connection connection;
	private String url, user, password;
	Properties properties;
	
	/**
	 * Constructor; Get the information (url, user, password) from the config.properties file.
	 * @throws IOException
	 */
	public DatabaseManager() throws IOException {
		FileInputStream input = new FileInputStream("src/main/resources/config.properties");
		properties = new Properties();
		properties.load(input);
		url = properties.getProperty("Database.url");
		user = properties.getProperty("Database.user");
		password = properties.getProperty("Database.password");
	}
	
	/**
	 * Connect to DB.
	 * @throws SQLException
	 */
	public void connect() throws SQLException {
		connection = DriverManager.getConnection(url, user, password);
		System.out.println("DatabaseManager: Connected to DB.");
		logger.logInfo(INSTANCE, "Connected to DB");
	}
	
	/**
	 * Close the connection.
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if (connection != null) {
			connection.close();
			System.out.println("DatabaseManager: Connection to DB closed.");
			logger.logInfo(INSTANCE, "Connection to DB closed");
		}
	}
	
	/**
	 * Function for executing SELECT queries.
	 * @param query
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String query) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);		
		return preparedStatement.executeQuery();
	}
	
	/**
	 * Function for executing INSERT, UPDATE and DELETE queries.
	 * @param query
	 * @return int
	 * @throws SQLException
	 */
	public int executeUpdate(String query) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		return preparedStatement.executeUpdate();
	}
}