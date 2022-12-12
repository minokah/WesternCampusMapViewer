
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class handles all user information, including usernames, passwords, etc.
 * 
 * The User constructor sets all of a user's attributes. We also deal with encryption handling and the handling of custom user data in this file.
 * 
 * @author jender, bluu8
 */

public class User {
    
    //The fields a user has: username, password, are they a developer, their AES encryption key, and a hash map of POIs and favourite POIs.
    private String username;
    private String password;
    private boolean developer;
    private String data;
    private SecretKeySpec aesSpec;
    
    // <Map Code, ArrayList of Floors> 
    private HashMap<String, ArrayList> poiData;
    private HashMap<String, ArrayList> poiFavourites;

    /**
     * The constructor is very simple, and just sets each of these fields.
     * 
     * @param user - The new username
     * @param pw - The new password
     * @param dev - Is this user a developer?
     * @param d - Any additional data for the user
     */
    public User(String user, String pw, boolean dev, String d) {
        username = user;
        password = pw;
        developer = dev;
        data = d;
        poiData = new HashMap();
        poiFavourites = new HashMap();
    }
    
    /**
     * Get the username.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Get the password.
     * 
     * @return The password.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Get the user's developer status
     * 
     * @return True or false
     */
    public boolean isDeveloper() {
        return developer;
    }
    
    /**
     * Get any additional user data
     * 
     * @return Any data, if it exists
     */
    public String getData() {
        return data;
    }
    
    /**
     * This function sets the AES key for a user.
     * 
     * @param aesSpec - The key to assign
     */
    public void setAESSpec(SecretKeySpec aesSpec) {
        this.aesSpec = aesSpec;
    }
    
    /**
     * Returns an ArrayList of floors with the user's custom POI data.
     * 
     * @param code - The building code to extract data from
     * @return An ArrayList of the user's custom POIs for that building
     */
    
    public ArrayList<Floor> getCustomData(String code) {
        return poiData.get(code);
    }
    
    
    /**
     * Returns an ArrayList of floors containing the data for the user's favourite built-in POIs
     * 
     * @param code - The building code to extract data from
     * @return An ArrayList of the user's favourite built-in POIs for that building
     */
    public ArrayList<Floor> getFavouritesData(String code) {
        return poiFavourites.get(code);
    }
    
    /**
     * Normalizes the building. In the case where the user is missing
     * the entry for the building or missing floors, this method will
     * fill in the blanks and fix that.
     * @param building The building object.
     */
    public void normalizeBuilding(Building building) {
        String code = building.getCode();
        ArrayList<Floor> buildingFloors = building.getFloors();
        
        // normalize the custom poi floors for the building
        if (!poiData.containsKey(code)) poiData.put(code, new ArrayList()); 
        ArrayList<Floor> userFloors = poiData.get(code);
        for (int i = 0; i != buildingFloors.size(); i++) {
            boolean has = false;
            for (int a = 0; a != userFloors.size(); a++) {
                if (userFloors.get(a).getID() == buildingFloors.get(i).getID()) {
                    has = true;
                    break;
                }
            }

            if (!has) {
                JSONObject newFloor = new JSONObject();
                newFloor.put("id", buildingFloors.get(i).getID());
                newFloor.put("poi", new JSONArray());
                poiData.get(code).add(new Floor(newFloor, false));
            }
        }
        
        // do the same with favourites
        if (!poiFavourites.containsKey(code)) poiFavourites.put(code, new ArrayList()); 
        ArrayList<Floor> favouriteFloors = poiFavourites.get(code);
        for (int i = 0; i != buildingFloors.size(); i++) {
            boolean has = false;
            for (int a = 0; a != favouriteFloors.size(); a++) {
                if (favouriteFloors.get(a).getID() == buildingFloors.get(i).getID()) {
                    has = true;
                    break;
                }
            }
            
            if (!has) {
                JSONObject newFloor = new JSONObject();
                newFloor.put("id", buildingFloors.get(i).getID());
                newFloor.put("poi", new JSONArray());
                poiFavourites.get(code).add(new Floor(newFloor, false));
            }
        }
    }
    
    /**
     * This method writes out all of the user data in JSON format.
     * 
     * We collect all of the buildings, all of the user's favourite POIs, and all of their data, and we push this out to our JSON
     * files.
     */
    public void writeToFile() {
        JSONObject toWrite = new JSONObject();
        JSONObject buildings = new JSONObject();
        JSONObject favourites = new JSONObject();
        
        // for each building
        for (String b : poiData.keySet()) {
            JSONArray floors = new JSONArray();
            
            // for each floor
            ArrayList<Floor> floorlist = poiData.get(b);
            for (int a = 0; a != floorlist.size(); a++) {
                // get their json and add it to the list
                floors.put(floorlist.get(a).exportJSON());
            }
            
            buildings.put(b, floors);
        }
        
        // favourites
        // for each building
        for (String b : poiFavourites.keySet()) {
            JSONArray floors = new JSONArray();
            
            // for each floor
            ArrayList<Floor> floorlist = poiFavourites.get(b);
            for (int a = 0; a != floorlist.size(); a++) {
                // get their json and add it to the list
                floors.put(floorlist.get(a).exportJSON());
            }
            
            favourites.put(b, floors);
        }
        
        toWrite.put("poi", buildings);
        toWrite.put("favourite", favourites);
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/user/" + username + ".json"));
            writer.write(toWrite.toString());
            writer.close();
            
            // after writing, encrypt data
            encryptData();
            // delete unencrypted data
            File file = new File("data/user/" + username + ".json");
            file.delete();
        }
        catch (IOException e) {
            System.out.println("Failed to write " + username + "'s data to their JSON!");
        }
    
    }
    
    /**
     * This method encrypts all of the data in a user's file - this is an added feature for security.
     * 
     * We use a cipher to encrypt the user's data, pass their data file through it, and output it to a .enc (encrypted) file.
     * 
     * @throws IOException if the system failed to find the user's file.
     */
    public void encryptData() throws IOException {
        
        try (FileInputStream input = new FileInputStream("data/user/" + username + ".json"); //Start a new file stream
        ){
            File fileOut = new File("data/user/" + username + ".enc"); //Make an encrypted output file for the user
            FileOutputStream output = new FileOutputStream(fileOut);
            // we use a constant zero initialization vector so we dont need to store
            // it for encryption or decryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesSpec, new IvParameterSpec(new byte[16]));
            
            processDataFile(cipher, input, output); //Encrypt the file
            output.close();
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Failed to find " + username + "'s data.");
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IOException ex) { //Catch any relevant exceptions
            throw new IOException("Failed to encrypt " + username + "'s data.");
        }
    }
    
    /**
     * This method processes an encrypted data file, writing the encrypted data to an output stream.
     * 
     * @param cipher - The cipher to use (see above)
     * @param input - The input file
     * @param output  - The output file
     */
    private void processDataFile(Cipher cipher, InputStream input, OutputStream output) {
        try {
            byte[] inBuffer = new byte[1024];
            byte[] outBuffer;
            int len = input.read(inBuffer);
            // go through file, read line, process it via the cipher
            // and write it to the output
            while (len != -1) {
                outBuffer = cipher.update(inBuffer, 0, len);
                if (outBuffer != null) output.write(outBuffer);
                outBuffer = null;
                len = input.read(inBuffer);
            }
            
            outBuffer = cipher.doFinal();
            if (outBuffer != null) output.write(outBuffer);
        } catch (IOException | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * This file decrypts an encrypted file into JSON format.
     * 
     * @throws IOException if the file wasn't found.
     */
    public void decryptData() throws IOException {
        
        // decrypt encrypted file into JSON format
        try ( 
            FileInputStream input = new FileInputStream("data/user/" + username + ".enc");
        ){
            File fileOut = new File("data/user/" + username + ".json");
            FileOutputStream output = new FileOutputStream(fileOut);
            // we use a constant zero initialization vector so we dont need to store
            // it for encryption or decryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, aesSpec, new IvParameterSpec(new byte[16]));
            processDataFile(cipher, input, output);
            output.close();
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Failed to find " + username + "'s data.");
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IOException ex) {
            throw new IOException("Failed decrypting " + username + "'s data.");
        }
    }
    
    /**
     * This file reads the custom data for a user *after* this data was encrypted.
     * 
     * We decrypt the user's file and then parse through it to make a hash map of their POIs, and deal with the rest of their information.
     * 
     * @throws IOException if the files weren't found
     * @throws JSONException if the JSON processing fails
     */
    public void readCustomData() throws IOException, JSONException {
        
        // read JSON file and set data
        try {
            // decrypt data file and load as JSON
            decryptData();
            JSONObject userFile = new JSONFileReader("data/user/" + username + ".json").getData();
                
            // read built-in pois
            JSONObject poiBuildings = userFile.getJSONObject("poi");

            // read the building codes from the data, and create a hashmap of the pois for each building
            for (String code : poiBuildings.keySet()) {
                ArrayList<Floor> floors = new ArrayList();
                JSONArray floorList = poiBuildings.getJSONArray(code);

                // for every single floor
                for (int a = 0; a != floorList.length(); a++) {
                    Floor fl = new Floor(floorList.getJSONObject(a), false);
                    floors.add(fl);
                }

                poiData.put(code, floors);
            }

            // read favourite pois
            JSONObject poiFavouritesJSON = userFile.getJSONObject("favourite");

            // read the building codes from the data, and create a hashmap of the pois for each building
            for (String code : poiFavouritesJSON.keySet()) {
                ArrayList<Floor> floors = new ArrayList();
                JSONArray floorList = poiFavouritesJSON.getJSONArray(code);

                // for every single floor
                for (int a = 0; a != floorList.length(); a++) {
                    Floor fl = new Floor(floorList.getJSONObject(a), false);
                    floors.add(fl);
                }

                poiFavourites.put(code, floors);
            }
            
            // delete decrypted data afterward
            File file = new File("data/user/" + username + ".json");
            file.delete();
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Failed to find " + username + "'s data.");
        } catch (IOException ex) {
            throw new IOException("Failed to decrypt " + username + "'s data.");
        } catch (JSONException ex) {
            throw new JSONException("Failed to read " + username + "'s data due to formatting or corruption.");
        }
    }
    
}
