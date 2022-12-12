/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author scottie
 */
public class BuildingTest {
    
    public BuildingTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        System.out.println("setUpClass()");
        }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("tearDownClass()");
    }
    
    @BeforeEach
    public void setUp() {
        System.out.println("setUp()");
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("tearDown()");
    }

    /**
     * Test of setName method, of class Building.
     */
    public Building setupMC(){
        try{
            Building testObj = new Building("mc");
            return testObj;
        
        }catch(FileNotFoundException e){
            System.out.println("filenotfound");
        }catch(JSONException e){
            System.out.println("JSONexception");
        }
        return null;
    }
    
    
    
    @org.junit.jupiter.api.Test
    public void testSetName() {
        System.out.println("setName");
        Building instance = setupMC();
        String expResult = "fakeName";
        instance.setName("fakeName");
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCode method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testSetCode() {
        System.out.println("setCode");
        Building instance = setupMC();
        String expResult = "fakeCode";
        instance.setCode("fakeCode");
        String result = instance.getCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testGetName() {
        System.out.println("getName");
        Building instance = setupMC();
        String expResult = "Middlesex College";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getCode method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testGetCode() {
        System.out.println("getCode");
        Building instance = setupMC();
        String expResult = "mc";
        String result = instance.getCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of addFloor method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testAddFloor() {
        System.out.println("addFloor");
        Building instance = setupMC();
        Floor fl = instance.getFloor(4);
        fl.setID(5);
        instance.addFloor(fl);
        int expResult = 6;
        int result = instance.getNumFloors();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getFloor method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testGetFloor() {
        System.out.println("getFloor");
        int i = 4;
        Building instance = setupMC();
        int expResult = 5;
        Floor result = instance.getFloor(i);
        assertEquals(expResult, result.getID());
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of removeFloor method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testRemoveFloor() {
        System.out.println("removeFloor");
        int i = 0;
        Building instance = setupMC();
        instance.removeFloor(4);
        int expResult = 4;
        int result = instance.getNumFloors();
        assertEquals(expResult, result);

    }

    /**
     * Test of getFloors method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testGetFloors() {
        System.out.println("getFloors");
        Building instance = setupMC();
        ArrayList<Floor> expResult = new ArrayList<>(Arrays.asList(instance.getFloor(0),instance.getFloor(1),instance.getFloor(2),instance.getFloor(3),instance.getFloor(4)));
        ArrayList<Floor> result = instance.getFloors();
        assertEquals(expResult, result);

    }

    /**
     * Test of getNumFloors method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testGetNumFloors() {
        System.out.println("getNumFloors");
        Building instance = setupMC();
        int expResult = 5;
        int result = instance.getNumFloors();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of moveFloorOrderUp method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testMoveFloorOrderUp() {
        System.out.println("moveFloorOrderUp");
        int i = 2;
        Building instance = setupMC();
        
        instance.moveFloorOrderUp(i);
        Floor testPost = instance.getFloor(i);
        System.out.println(testPost.getID());
        int expResult = i;
        int result = testPost.getID();
        assertEquals(expResult, result);

    }

    /**
     * Test of moveFloorOrderDown method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testMoveFloorOrderDown() {
        System.out.println("moveFloorOrderDown");
        int i = 1;
        Building instance = setupMC();
        instance.moveFloorOrderDown(i);
        Floor testPost = instance.getFloor(i);
        int expResult = i+2;
        int result = testPost.getID();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHighestID method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testGetHighestID() {
        System.out.println("getHighestID");
        Building instance = setupMC();
        int expResult = 5;
        int result = instance.getHighestID();
        System.out.println(result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of incrementID method, of class Building.
     */
    @org.junit.jupiter.api.Test
    public void testIncrementID() {
        System.out.println("incrementID");
        Building instance = setupMC();
        instance.incrementID();
        int expResult = 6;
        int result = instance.getHighestID();
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(expResult, result);
    }   
}
