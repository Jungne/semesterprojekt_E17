/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testdatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jungn
 */
public class TestDatabase {

    static ResultSet rs;
    static DBManager dbm;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	dbm = DBManager.getInstance();
	test3();
    }

    public static void test() {
	try {
	    dbm.executeUpdate("CREATE TABLE test ("
		    + "col1 INTEGER,"
		    + "col2 VARCHAR(255))");

	} catch (SQLException ex) {
	    Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public static void test2() {
	try {
	    dbm.executeUpdate("INSERT INTO test(col1, col2)"
		    + "VALUES (12, 'ok')");

	} catch (SQLException ex) {
	    Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public static void test3() {
	try {
	    rs = dbm.executeQuery("SELECT *"
		    + "FROM test");
	    while (rs.next()) {
		System.out.println(rs.getInt(1));
		System.out.println(rs.getString(2));
		
	    }

	} catch (SQLException ex) {
	    Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    //drops the table
    public static void test4() {
	try {
	    dbm.execute("DROP TABLE test");

	} catch (SQLException ex) {
	    Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
