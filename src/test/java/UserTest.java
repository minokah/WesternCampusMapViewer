/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author akazache
 */
public class UserTest {
    
    public UserTest() {
    }
    
    @BeforeEach
    public void setUp() {
        File file = new File("data/user/fakeUser.enc");
        file.delete();
        File file2 = new File("data/user/fakeUser.json");
        file2.delete();
    }
    
    @AfterAll
    public void tearDownClass() {
        File file = new File("data/user/fakeUser.enc");
        file.delete();
        File file2 = new File("data/user/fakeUser.json");
        file2.delete();
    }

    /**
     * Test of getUsername method, of class User.
     */
    @Test
    public void testGetUsername() {
        System.out.println("Testing getUsername");
        User instance = new User("testName", "testPassword", false, "testData");
        String expResult = "testName";
        String result = instance.getUsername();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPassword method, of class User.
     */
    @Test
    public void testGetPassword() {
        System.out.println("Testing getPassword");
        User instance = new User("testName", "testPassword", false, "testData");
        String expResult = "testPassword";
        String result = instance.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of isDeveloper method, of class User.
     */
    @Test
    public void testIsDeveloper() {
        System.out.println("Testing isDeveloper");
        User instance = new User("testName", "testPassword", false, "testData");
        boolean expResult = false;
        boolean result = instance.isDeveloper();
        assertEquals(expResult, result);
    }

    /**
     * Test of getData method, of class User.
     */
    @Test
    public void testGetData() {
        System.out.println("Testing getData");
        User instance = new User("testName", "testPassword", false, "testData");
        String expResult = "testData";
        String result = instance.getData();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomData method, of class User.
     */
    @Test
    public void testReadCustomDataForNoData() {
        
        File file = new File("data/user/fakeUser.enc");
        file.delete();
        System.out.println("Testing readCustomData -- testing for no such data");
        User instance = new User("fakeUser", "fakePassword", false, "fakeData");
        assertThrows(FileNotFoundException.class, ()->{
            instance.readCustomData();
        });
    }
    
    @Test
    public void testReadCustomDataForNotJSON() {

        System.out.println("Testing readCustomData -- test for data not in JSON format");
        // password is 1234
        String password = "103,-34,116,-109,-46,37,-5,-83,-104,98,50,-75,-17,95,-110,36";
        User instance = new User("fakeUser", password, false, "fakeData");
        instance.setAESSpec(generateAESSpec("aaaa"));
        
        try (FileWriter fw = new FileWriter("data/user/fakeUser.json")) {
            fw.write("aaaa");
            instance.encryptData();
        } catch (IOException e) {
            System.out.println(e);
        }
        
        assertThrows(JSONException.class, ()->{
            instance.readCustomData();
        });
    }
    
    private SecretKeySpec generateAESSpec(String password) {
        
        try {
            // we use a different salt for the encrypting data than hashing password
            // this is so someone cant just read the hashed password from the file
            // and use that to decrypt the data
            byte[] salt = new byte[16]; 
            Arrays.fill(salt, (byte) 1); 
            
            // hash password, then generate the corresponding AES key
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashPassword = keyFactory.generateSecret(keySpec).getEncoded();
            SecretKeySpec aesSpec = new SecretKeySpec(hashPassword, "AES");
            return aesSpec;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            System.out.println("Failed to generate AES key.");
            return null;
        }
        
    }
    
}
