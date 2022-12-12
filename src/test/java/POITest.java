/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mdrit
 */
public class POITest {
    
    public POITest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getName method, of class POI.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        String expResult = "POI";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class POI.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        POI instance = new POI("Test POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        String expResult = "Fake Name";
        instance.setName(expResult);
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCategory method, of class POI.
     */
    @Test
    public void testGetCategory() {
        System.out.println("getCategory");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        String expResult = "Test POI";
        String result = instance.getCategory();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCategory method, of class POI.
     */
    @Test
    public void testSetCategory() {
        System.out.println("setCategory");
        POI instance = new POI("Test POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        String expResult = "Fake Cat";
        instance.setCategory(expResult);
        String result = instance.getCategory();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIcon method, of class POI.
     */
    @Test
    public void testGetIcon() {
        System.out.println("getIcon");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        String expResult = "default";
        String result = instance.getIcon();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIcon method, of class POI.
     */
    @Test
    public void testSetIcon() {
        System.out.println("setIcon");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        String expResult = "restoommale";
        instance.setIcon(expResult);
        String result = instance.getIcon();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDescription method, of class POI.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        String expResult = "This is a test POI.";
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDescription method, of class POI.
     */
    @Test
    public void testSetDescription() {
        System.out.println("setDescription");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        String expResult = "This is a fake description.";
        instance.setDescription(expResult);
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of getX method, of class POI.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        int expResult = 1500;
        int result = instance.getX();
        assertEquals(expResult, result);
    }

    /**
     * Test of setX method, of class POI.
     */
    @Test
    public void testSetX() {
        System.out.println("setX");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        int expResult = 1750;
        instance.setX(expResult);
        int result = instance.getX();
        assertEquals(expResult, result);
    }

    /**
     * Test of getY method, of class POI.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        int expResult = 1500;
        int result = instance.getY();
        assertEquals(expResult, result);
    }

    /**
     * Test of setY method, of class POI.
     */
    @Test
    public void testSetY() {
        System.out.println("setY");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        int expResult = 1750;
        instance.setY(expResult);
        int result = instance.getY();
        assertEquals(expResult, result);
    }

    /**
     * Test of setBuiltIn method, of class POI.
     */
    @Test
    public void testSetBuiltIn() {
        System.out.println("setBuiltIn");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        boolean expResult = false;
        instance.setBuiltIn(expResult);
        boolean result = instance.isBuiltIn();
        assertEquals(expResult, result);
    }

    /**
     * Test of isBuiltIn method, of class POI.
     */
    @Test
    public void testIsBuiltIn() {
        System.out.println("isBuiltIn");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        boolean expResult = false;
        instance.setBuiltIn(expResult);
        boolean result = instance.isBuiltIn();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFavourited method, of class POI.
     */
    @Test
    public void testSetFavourited() {
        System.out.println("setFavourited");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        boolean expResult = true;
        instance.setFavourited(expResult);
        boolean result = instance.isFavourited();
        assertEquals(expResult, result);
    }

    /**
     * Test of isFavourited method, of class POI.
     */
    @Test
    public void testIsFavourited() {
        System.out.println("isFavourited");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        boolean expResult = false;
        boolean result = instance.isFavourited();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEquivalent method, of class POI.
     */
    @Test
    public void testIsEquivalent() {
        System.out.println("isEquivalent");
        POI instanceOne = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        POI instanceTwe = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        boolean expResult = true;
        boolean result = instanceOne.isEquivalent(instanceTwe);
        assertEquals(expResult, result);
    }

    /**
     * Test of exportJSON method, of class POI.
     */
    @Test
    public void testExportJSON() {
        System.out.println("exportJSON");
        POI instance = new POI("POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        
        JSONObject newPOIJSON = new JSONObject();
        newPOIJSON.put("name", "POI");
        newPOIJSON.put("category", "Test POI");
        newPOIJSON.put("icon", "default");
        newPOIJSON.put("description", "This is a test POI.");
        newPOIJSON.put("x", 1500);
        newPOIJSON.put("y", 1500);
        
        String expResult = newPOIJSON.toString();
        String result = instance.exportJSON().toString();
        assertEquals(expResult, result);
    }

}
