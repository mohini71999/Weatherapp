package MyPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class First extends HttpServlet {
	
	
       
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String input=req.getParameter("city");
	System.out.println(input);
		
		String apiKey = "7d605b7925a59438f3fe3bdc586e6379";
		// Get the city from the form input
        String city = req.getParameter("city"); 
        //create url
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
		//called URL object
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
                //reading the data from network
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
               // want to store data
                StringBuilder responseContent = new StringBuilder();
                //take input and give scanner class as a input
                Scanner scanner = new Scanner(reader);
                
                while (scanner.hasNext()) {
                    responseContent.append(scanner.nextLine());
                }
                //closing the scanner input
                scanner.close();
                
                // Parse the JSON response to extract temperature, date, and humidity
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
                //req.setAttribute("date", date);
                req.setAttribute("city", city);
                req.setAttribute("temperature", temperatureCelsius);
                req.setAttribute("weatherCondition", weatherCondition); 
                req.setAttribute("humidity", humidity);    
                req.setAttribute("windSpeed", windSpeed);
                req.setAttribute("weatherData", responseContent.toString());
                
                connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Forward the request to the weather.jsp page for rendering
        req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
	
	
	
	
	
}
