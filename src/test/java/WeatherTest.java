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
 * @author mdrit
 */
public class WeatherTest {
    
    public WeatherTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("WeatherTest.setUpClass()");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("WeatherTest.tearDownClass()");
    }
    
    @BeforeEach
    public void setUp() {
        System.out.println("WeatherTest.setUp()");
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("WeatherTest.tearDown()");
    }

    /**
     * Test of updateWeatherData method, of class Weather.
     */
    @org.junit.jupiter.api.Test
    public void testUpdateWeatherData() {
        System.out.println("updateWeatherData");
        Weather instance = new Weather();
        boolean expResult = true;
        boolean result = instance.updateWeatherData();
        assertEquals(expResult, result);
    }
    
}
