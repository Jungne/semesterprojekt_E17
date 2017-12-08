/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import interfaces.Trip;
import interfaces.User;
import java.util.ArrayList;
import java.util.List;
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
public class TripsFunctionalityTest {
    
    public TripsFunctionalityTest() {
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
    public void testParticipateMethod() {
        List<User> listOfparticipants = new ArrayList<User>();
        Trip mytrip = new Trip(1234, "TITTLE", "description", null, 1.00, null, null, "meetingAddress", 100, null, listOfparticipants, null, null, null, null, null);
       User user = new User(123,"adam@hotmail.com","Adam", null); 
      mytrip.participate(user);
       Assert.assertEquals(mytrip.getParticipants().get(0),user);
    }
}
