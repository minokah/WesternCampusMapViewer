import org.json.JSONObject;

/**
 * This class handles the construction and data manipulation for points of interest (POIs).
 * 
 * Each floor of each building has a number of user-made and built-in points of interest, which include
 * classrooms, stairwells, etc. This file encapsulates POIs as objects and handles their manipulation and
 * construction.
 * 
 * @author bluu8, jender
 */
public class POI {
    //Each POI has a name, a category (e.g. classroom), an icon, a description, (x,y) coordinates on
    //the screen, and booleans denoting if it is built in, and if it is favourited by the user.
    private String name;
    private String category;
    private String icon;
    private String description;
    private int x;
    private int y;
    private boolean builtin;
    private boolean favourited;
    
    /**
     * The constructor for POIS simply sets all of its attributes.
     * 
     * @param newName - The name of the POI
     * @param newCat - The category for the POI
     * @param iconPath - The file path for the POI's icon
     * @param newDescription - The description of the POI
     * @param x1 - The POI's x coordinate
     * @param y1 - The POI's y coordinate
     */
    public POI(String newName, String newCat, String iconPath, String newDescription, int x1, int y1) {
        name = newName;
        category = newCat;
        icon = iconPath;
        description = newDescription;
        x = x1;
        y = y1;
        
        // default favourited as false
        favourited = false;
    }
    
    /**
     * This method returns the name of the POI.
     * 
     * @return The name of the POI.
     */
    public String getName() {
        return name;
    }
    
    /**
     * This method sets a new name for a POI.
     * 
     * @param newName - The new name for the POI.
     */
    public void setName(String newName) {
        name = newName;
    }
    
    /**
     * This method gets the category for a POI.
     * 
     * @return The category of the POI.
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * This method sets a new category for the POI.
     * 
     * @param newCategory - The new category to set.
     */
    public void setCategory(String newCategory) {
        category = newCategory;
    }
    
    /**
     * This method gets the file path for the POI's icon.
     * 
     * @return The icon's path.
     */
    public String getIcon() {
        return icon;
    }
    
    /**
     * This method sets a new icon based on a given file path.
     * 
     * @param iconPath - The path to the new icon.
     */
    public void setIcon(String iconPath) {
        icon = iconPath;
    }
    
    /**
     * This method gets the description for the POI.
     * 
     * @return The description of the POI.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * This method sets a new description for the POI.
     * 
     * @param desc - The new description.
     */
    public void setDescription(String desc) {
        description = desc;
    }
    
    /**
     * This function returns the x-coordinate of the POI.
     * 
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }
    
    /**
     * This functions sets the x-coordinate for a POI.
     * 
     * @param x1 - The new x-coordinate.
     */
    public void setX(int x1) {
        x = x1;
    }
     
    /**
     * This function returns the y-coordinate of the POI.
     * 
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }
    
    /**
     * This functions sets the y-coordinate for a POI.
     * 
     * @param y1 - The new y-coordinate.
     */
    public void setY(int y1) {
        y = y1;
    }
    
    /**
     * This function sets the built-in status of a POI.
     * 
     * @param val - True or false.
     */
    public void setBuiltIn(boolean val) {
        builtin = val;
    }
    
    /**
     * This function returns the built-in status of a POI.
     * 
     * @return True or false.
     */
    public boolean isBuiltIn() {
        return builtin;
    }
    
    /**
     * This function sets the favourited status of a POI.
     * 
     * @param val - True or false.
     */
    public void setFavourited(boolean val) {
        favourited = val;
    }
    
    /**
     * This function returns the favourited status of a POI.
     * 
     * @return True or false.
     */
    public boolean isFavourited() {
        return favourited;
    }
    
    /**
     * This function checks if two POIs are equal, in a natural way.
     * 
     * If every attribute of the two POIs is the same, this function returns true.
     * 
     * @param poi - The POI to check on.
     * @return True or false.
     */
    public boolean isEquivalent(POI poi) {
        if (poi.getName().equals(name) && poi.getCategory().equals(category) && poi.getDescription().equals(description) && poi.getIcon().equals(icon) && poi.getX() == x && poi.getY() == y) return true;
        return false;
    }
    
    /**
     * This method exports all of the information for a POI as a JSON object.
     * 
     * @return A JSON object containing all of the relevant data for this POI.
     */
    public JSONObject exportJSON() {
        JSONObject toWrite = new JSONObject();
        toWrite.put("name", name);
        toWrite.put("category", category);
        toWrite.put("icon", icon);
        toWrite.put("description", description);
        toWrite.put("x", x);
        toWrite.put("y", y);
        
        return toWrite;
    }
}
