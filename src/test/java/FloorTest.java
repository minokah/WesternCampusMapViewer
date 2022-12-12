/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
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
public class FloorTest {
    
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
     * Test of updateMap method, of class Floor.
     */
    @Test
    public void testUpdateMap() {
        System.out.println("updateMap");
        
        POI poi = new POI("Test POI", "Test POI", "default", "This is a test POI.", 1500, 1500);
        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        data.put("poi", new JSONArray());
        
        Floor instance = new Floor(data, true);
        instance.addPOI(poi);
        instance.removePOI(poi);
        
        boolean result = instance.getPOIs().keySet().contains("Test POI");
        assertEquals(false, result);
    }

    /**
     * Test of getID method, of class Floor.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
        
        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        data.put("poi", new JSONArray());
        
        Floor instance = new Floor(data, true);
        
        int expResult = 1;
        int result = instance.getID();
        assertEquals(expResult, result);
    }

    /**
     * Test of setID method, of class Floor.
     */
    @Test
    public void testSetID() {
        System.out.println("setID");
        
        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        data.put("poi", new JSONArray());
        
        Floor instance = new Floor(data, true);
        
        int expResult = 2;
        instance.setID(2);
        int result = instance.getID();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Floor.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        
        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        data.put("poi", new JSONArray());
        
        Floor instance = new Floor(data, true);
        
        String expResult = "Test Floor";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class Floor.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        
        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        data.put("poi", new JSONArray());
        
        Floor instance = new Floor(data, true);
        
        String expResult = "New Test Floor";
        instance.setName("New Test Floor");
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of addPOI method, of class Floor.
     */
    @Test
    public void testAddPOI() {
        System.out.println("addPOI");
        
        POI poi = new POI("Test POI", "Test", "default", "This is a test POI.", 1500, 1500);
        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        data.put("poi", new JSONArray());
        
        Floor instance = new Floor(data, true);
        instance.addPOI(poi);
        
        HashMap<String, ArrayList> resultHashmap = instance.getPOIs();
        ArrayList<POI> resultList = resultHashmap.get("Test");
        assertEquals(true, resultList.contains(poi));
    }

    /**
     * Test of removePOI method, of class Floor.
     */
    @Test
    public void testRemovePOI() {
        System.out.println("removePOI");
        
        POI poi = new POI("Test POI", "Test", "default", "This is a test POI.", 1500, 1500);
        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        data.put("poi", new JSONArray());
        
        Floor instance = new Floor(data, true);
        instance.addPOI(poi);
        instance.removePOI(poi);
        
        HashMap<String, ArrayList> resultHashMap = instance.getPOIs();
        boolean result = resultHashMap.containsKey("Test");
        assertEquals(false, result);
    }

    /**
     * Test of getPOIs method, of class Floor.
     */
    @Test
    public void testGetPOIs() {
        System.out.println("getPOIs");
        
        POI poi = new POI("Test POI", "Test", "default", "This is a test POI.", 1500, 1500);
        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        data.put("poi", new JSONArray());
        
        Floor instance = new Floor(data, true);
        instance.addPOI(poi);
        HashMap<String, ArrayList> resultHashMap = instance.getPOIs();
        ArrayList<POI> resultList = resultHashMap.get("Test");
        boolean result = (resultList.contains(poi) && (resultList.size() == 1));
        assertEquals(true, result);
    }

    /**
     * Test of exportJSON method, of class Floor.
     */
    @Test
    public void testExportJSON() {
        System.out.println("exportJSON");

        JSONObject data = new JSONObject();
        data.put("name", "Test Floor");
        data.put("id", 1);
        
        JSONArray poiArray = new JSONArray();
        JSONObject newPOIJSON = new JSONObject();
        newPOIJSON.put("name", "Test POI");
        newPOIJSON.put("category", "Test");
        newPOIJSON.put("icon", "default");
        newPOIJSON.put("description", "This is a test POI.");
        newPOIJSON.put("x", 1500);
        newPOIJSON.put("y", 1500);
        poiArray.put(newPOIJSON);
        data.put("poi", poiArray);
       
        POI poi = new POI("Test POI", "Test", "default", "This is a test POI.", 1500, 1500);
        Floor instance = new Floor(data, true);
        //instance.addPOI(poi);
       
        String expResult = data.toString();
        String result = instance.exportJSON().toString();
        assertEquals(expResult, result);
    }
}

