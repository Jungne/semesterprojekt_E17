package database;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBManager {

	private Connection connection;
	private static DBManager dbManager = null;

	private DBManager() {
		String url = "jdbc:postgresql://tek-mmmi-db0a.tek.c.sdu.dk/si3_2017_group_12_db";
		String user = "si3_2017_group_12";
		String password = "TGIF27-Ricky";
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("Connection to database successful!");
		} catch (SQLException ex) {
			System.out.println("Connection to database failed.");
		}
	}

	public static DBManager getInstance() {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}

	public void execute(String query) {
		try (Statement statement = connection.createStatement()) {
			statement.execute(query);
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void executeUpdate(String query) {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public ResultSet executeQuery(String query) {
		try {
			return connection.createStatement().executeQuery(query);
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public int executeInsertAndGetId(String query) {
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
			return -1;
		}
	}

	/**
	 * A very specific method for executing a statement, where the first argument
	 * an image.
	 *
	 * @param query
	 * @param image
	 * @return
	 */
	public int executeImageInsertAndGetId(String query, byte[] image) {
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			statement.setBinaryStream(1, new ByteArrayInputStream(image));
			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
			return -1;
		}
	}

	public PreparedStatement getPreparedStatement(String query) {
		try {
			return connection.prepareStatement(query);
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
}
