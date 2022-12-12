import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
*  This class handles user login based on encrypted user information stored in JSON format.
* 
*  New users set a password, which is then encrypted and stored along with their username in
*  /data/user.json.
* 
*  @author bluu8, akazache, jender
*/
public class Login {

    private HashMap<String, User> users;
    
    /** 
     * The constructor pulls in all of the users into a hash map.
     * 
     */
    public Login() {
        try {
            users = new HashMap();

            // read all user json data
            JSONArray jsonUsers = new JSONFileReader("data/user.json").getData().getJSONArray("users");
            
            // iterate and create User entries in map
            for (int i = 0; i != jsonUsers.length(); i++) {
                JSONObject userData = jsonUsers.getJSONObject(i);
                String name = userData.getString("name");
                String password = userData.getString("password");
                boolean isDeveloper = userData.getBoolean("isDeveloper");
                String data = userData.getString("data");
                
                users.put(name, new User(name, password, isDeveloper, data));
            }
        } catch (FileNotFoundException e) { //Error out if for some reason the file isn't found
            System.out.println(e);
        }
    }

    /**
     * This function verifies the login that a users enters.
     * 
     * Users enter a username and password, which is then verified against the image of their password
     * under the password hashing function (see below). This method will always return, regardless
     * of the outcome of the verification.
     * 
     * @param username the user-entered username
     * @param password the user-entered password
     * @return user on a match, null if their information doesn't check out
     */
    public User verifyLogin(String username, String password) {
        User user = users.get(username); //Pull their username from the dictionary
        if (user == null) { //Error out if this username doesn't exist
            return null;
        }
        try {
            // first hash the input password
            byte[] salt = new byte[16]; // fixed non-random salt
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); //Hash this input
            byte[] hashBytes = keyFactory.generateSecret(keySpec).getEncoded(); 
            
            
            String[] hashedBytes = user.getPassword().split(",");
            Boolean match = true;
            // check if mismatch between previously hashed bytes and our bytes
            for (int i = 0; i < hashedBytes.length; i++) {
                if (hashBytes[i] != (byte) Integer.parseInt(hashedBytes[i])) {
                    match = false;
                }
            }
            
            //Return this User object on match
            if (match) {
                user.setAESSpec(generateAESSpec(password));
                user.readCustomData();
                return user;
            }

        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) { //Simple error handling
            System.out.println("Failed to hash password.");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        return null;
    }
    
    /**
     * This method generates an AES key given a plain-text password.
     * 
     * This method takes in a plain-text password provided by the user, and generates a secret AES
     * key using tools from javax.crypto. 
     * 
     * @param password the user-provided password that we need to generate a key for
     * @return either a secret key corresponding to the user's password, or null on error
     * @author akazache
     */
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
