
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains the constructor and basic getters and setters for Building objects, including handling floors.
 * 
 * Includes a constructor to generate floors from a JSON file, plus getters and setters for each relevant
 * attribute of buildings.
 * 
 * @author bluu8
 */
public class Building {
    //Each building has a name, building code and an array of Floor objects.
    private String name;
    private String code;
    private ArrayList<Floor> floors;
    
    // this is the highest ID for the floors
    // when we make a new floor it will use this ID + 1
    private int highestID = 0;
    
    
    /**
     * This is the constructor for a Building object, that sets up all of the relevant information.
     * 
     * The constructor will take in the name of the new building, read in the JSON attributes for it,
     * and then generate an ArrayList of floor objects based on this. 
     * 
     * @param newName the name of the building that we're generating
     * @throws FileNotFoundException if the JSON file for the building doesn't exist
     * @throws JSONException if the JSON reader fails (e.g, data corruption) 
     */
    public Building(String newName) throws FileNotFoundException, JSONException {
        code = newName; //The building code
        floors = new ArrayList(); //Empty list of Floor objects
        
        // Prevent reading data if we have a building template
        if (newName == null) return;
        
        //New JSON reader for this file
        JSONObject data = new JSONFileReader("data/building/" + newName + "/data.json").getData();

        //Pull the name and floor attributes from the JSON file
        name = data.getString("name");
        JSONArray jsonFloors = data.getJSONArray("floors");

        // for each array floor in the json, make a Floor object 
        for (int i = 0; i != jsonFloors.length(); i++) {
            Floor newFloor = new Floor(jsonFloors.getJSONObject(i), true);
            
            // update highest id (auto-increment)
            if (newFloor.getID() > highestID) highestID = newFloor.getID();
            
            floors.add(newFloor);
        }
    }
    
    /**
     * This method sets the name of a building based on the input.
     * 
     * @param newName - The name of the building.
     */
    public void setName(String newName) {
        name = newName;
    }
    
    /**
     * This method sets the code for a building based on the input.
     * 
     * @param code - The building coe.
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * This method returns the name of the building object.
     * 
     * @return name - The name of the building
     */
    public String getName() {
        return name;
    }
    
    /**
     * This method returns the building code.
     * 
     * @return code - The building code.
     */
    public String getCode() {
        return code;
    }
    
    /**
     * This file adds a new Floor object to the ArrayList of all the floors of the building.
     * 
     * A basic call to ArrayList.add().
     * 
     * @param fl - The new floor object to be added.
     */
    public void addFloor(Floor fl) {
        floors.add(fl);
    }
    
    /**
     * This returns the Floor object at the specified index of the ArrayList.
     * 
     * A simple call to ArrayList.get().
     * 
     * @param index - the index of the floor we're looking for.
     * @return The floor at that index, if it exists.
     */
    public Floor getFloor(int index) {
        return floors.get(index);
    }
    
        /**
     * This removes the Floor object at the specified index of the ArrayList, if it exists.
     * 
     * A simple call to ArrayList.remove().
     * 
     * @param index - the index of the floor we're looking for.
     */
    public void removeFloor(int index) {
        floors.remove(index);
    }
    
    /**
     * This method returns the entire list of floor objects.
     * 
     * @return The entire list of floors of this building.
     */
    public ArrayList<Floor> getFloors() {
        return floors;
    }
    
    /**
     * This method returns the number of floors in this building.
     * 
     * @return The size of the ArrayList of the floors.
     */
    public int getNumFloors() {
        return floors.size();
    }
    
    /**
     * This method adjusts the index of a given floor, so that we can rearrange the order if we need to.
     * 
     * Takes in an index, stores it in a temporary variable, then moves the position of this floor up
     * by one.
     * 
     * @param index - The index of the floor we're modifying.
     */
    public void moveFloorOrderUp(int index) {
        Floor stored = floors.get(index);
        floors.add(index - 1, stored); //Move up the index of the floor
        floors.remove(index + 1); //Remove the old, redundant entry
    }
    
      /**
     * This method adjusts the index of a given floor, so that we can rearrange the order if we need to.
     * 
     * Takes in an index, stores it in a temporary variable, then moves the position of this floor down
     * by one.
     * 
     * @param index - The index of the floor we're modifying.
     */
    public void moveFloorOrderDown(int index) {
        Floor stored = floors.get(index);
        floors.add(index + 2, stored);  //Move down the index of the floor
        floors.remove(index); //Remove the old, redundant entry
    }
    
    /**
     * This method returns the highest floor index for this building.
     * 
     * @return The highest floor ID.
     */
    public int getHighestID() {
        return highestID;
    }
    
    /**
     * This method increments the highest floor index for the building.
     */
    public void incrementID() {
        highestID++;
    }
    
    /**
     * This method writes all of the JSON information for this building to the building's data file
     * in data/building/[building code]/data.json.
     * 
     * We pull in each Floor object in the ArrayList, and then write this out to the appropriate file, along
     * with the name and code of the building.
     * 
     */
    public void writeToFile() {
        JSONObject toWrite = new JSONObject();
        toWrite.put("name", name); //Push the name and code of the building
        toWrite.put("code", code);
        
        //Make a new JSON array to store the floors
        JSONArray floorjson = new JSONArray();

        // loop through each floor and get json
        for (int i = 0; i != floors.size(); i++) {
            floorjson.put(floors.get(i).exportJSON());
        }
        
        //Push this array
        toWrite.put("floors", floorjson);
        
        
        //Write to the appropriate building file in data/building. See this folder for some examples.
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/building/" + code + "/data.json"));
            writer.write(toWrite.toString());
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Failed to write POI data to data.json!");
        }
    }
 }
