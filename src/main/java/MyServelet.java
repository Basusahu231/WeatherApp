

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class MyServelet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Setup API
		String apiKey = "c9bc0feab8ce5d0234f3f7d49969d333";
		// Get the city from the form input
        String city = request.getParameter("city"); 

        // Create the URL for the OpenWeatherMap API request
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
// API Integration
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
       
        // Reading the data from network
        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        
        // want to store in string 
        StringBuilder responseContent = new StringBuilder();
  // Input lene ke liye from the reader
        Scanner scanner = new Scanner(reader);
        
        while(scanner.hasNext())
        {
        	responseContent.append(scanner.nextLine());
        }
        scanner.close();
        System.out.println(responseContent);
        // TYPE CASTING = parshing the data into JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);

 //Date & Time
 long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
 String date = new Date(dateTimestamp).toString();
 
 //Temperature
 double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
 int temperatureCelsius = (int) (temperatureKelvin - 273.15);

 //Humidity
 int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
 
 //Wind Speed
 double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
 
 //Weather Condition
 String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

 // Set the data as request attributes (for sending to the jsp page)
 request.setAttribute("date", date);
 request.setAttribute("city", city);
 request.setAttribute("temperature", temperatureCelsius);
 request.setAttribute("weatherCondition", weatherCondition); 
 request.setAttribute("humidity", humidity);    
 request.setAttribute("windSpeed", windSpeed);
 request.setAttribute("weatherData", responseContent.toString());
 request.getRequestDispatcher("index.jsp").forward(request, response);
 connection.disconnect();
} 

// Forward the request to the weather.jsp page for rendering
//request.getRequestDispatcher("index.jsp").forward(request, response);

 
	

}
