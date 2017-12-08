/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import interfaces.Message;
import interfaces.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Laura
 */
public class MessagingFunctionalityTest {
    
    public MessagingFunctionalityTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void comparatorTesting() {
     String str1 = "2014-04-08 12:30";
     String str2 = "2014-04-08 13:30";
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
LocalDateTime timeForOne = LocalDateTime.parse(str1, formatter);
LocalDateTime timeForTwo = LocalDateTime.parse(str1, formatter);
     Message m1 = new Message(11, null, "Hello, how are you?", timeForOne, 12);
     Message m2 = new Message(12, null, "I'm fine. Thank you",timeForTwo, 12 );
     Assert.assertEquals(-1,m1.compareTo(m2));
     }
}
