package testdatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void execute(String query) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    statement.execute(query);
	}
    }

    public void executeUpdate(String query) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    statement.executeUpdate(query);
	}
    }

    public ResultSet executeQuery(String query) throws SQLException {
	return connection.createStatement().executeQuery(query);
    }
}
