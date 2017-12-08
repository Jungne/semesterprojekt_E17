/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import interfaces.Category;
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
public class UsersFunctionalityTest {
    
    public UsersFunctionalityTest() {
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
    public void promoteToInstructorTest() {
        Category cat1 = new Category (13,"SCUBA DIVING");
     User user = new User(123,"adam@hotmail.com","Adam",null); 
     user.promoteToInstructor(cat1);
     Assert.assertEquals(cat1,user.getCertificates().get(0));
    }
}
