import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

/**
 * This class deals with our additional weather feature.
 * 
 * Weather is stored as an object with a temperature, a date, and a state.
 * 
 * @author bluu8, jender
 */
public class Weather {

    // https://open-meteo.com/en (the weather API we use)
    private double temperature = 0;
    private String date = null;
    private String state = null;

    /**
     * The constructor just calls the method below.
     */
    public Weather() {
        updateWeatherData();
    }

    /**
     * This function calls the open-meteo API, and grabs the weather data.
     * 
     * Data is parsed into JSON format so we can use it in our program.
     * 
     * @return True on success, false on failure
     */
    public boolean updateWeatherData() {
        try {
            //Grab weather at London, ON's coordinates
            URL url = new URL("https://api.open-meteo.com/v1/forecast?latitude=43.00&longitude=-81.27&current_weather=true&timezone=America/Detroit");
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            String result = new String(is.readAllBytes()); //Pull data from input stream
            is.close();
            
            //Parse into JSON format
            JSONObject data = new JSONObject(result).getJSONObject("current_weather");
            temperature = data.getDouble("temperature");
            date = (data.getString("time")).replace("T", " @ ");
            state = "nothing";

            return true;
        } catch (IOException e) {
            System.out.println("Failed to get weather data!");
            return false;
        }
    }

    /**
     * Get the current temperature in London
     * @return The temperature
     */
    public double getTemperature() {
        return temperature;
    }
    
    /**
     * Get the date
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * Get the state of the Weather object
     * @return The current state
     */
    public String getState() {
        return state;
    }
}
