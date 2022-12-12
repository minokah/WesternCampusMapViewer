/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Alex
 */
public class LoginTest {
    
    public LoginTest() {
    }

    /**
     * Test of verifyLogin method, of class Login.
     */
    @Test
    public void testVerifyLoginCorrect() {
        System.out.println("Testing verifyLogin -- can login correctly");
        String username = "Alex";
        String password = "aaaa";
        Login instance = new Login();
        User expResult = new User(username, "89,-101,-109,-103,-46,-79,13,-109,101,120,51,-52,28,-40,62,-105", false, "");
        User result = instance.verifyLogin(username, password);
        assertEquals(expResult.getUsername(), result.getUsername());
        assertEquals(expResult.getPassword(), result.getPassword());
        assertEquals(expResult.isDeveloper(), result.isDeveloper());
        assertEquals(expResult.getData(), result.getData());
    }
    
    @Test
    public void testVerifyLoginIncorrectly() {
        System.out.println("Testing verifyLogin -- returns null for wrong password");
        String username = "Alex";
        String password = "1234";
        Login instance = new Login();
        User result = instance.verifyLogin(username, password);
        assertNull(result);
    }
    
}
