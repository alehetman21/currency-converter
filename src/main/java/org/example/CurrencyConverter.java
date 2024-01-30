package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class CurrencyConverter {
    private static final String API_ENDPOINT = "https://v6.exchangerate-api.com/v6/%s/latest";
    private static final String API_KEY = System.getenv("API_KEY");

    public JsonObject getExchangeRates(String baseCurrency){
        try{
            HttpsURLConnection connection = getHttpsURLConnection(baseCurrency);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            return JsonParser.parseString(response.toString()).getAsJsonObject();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private HttpsURLConnection getHttpsURLConnection(String baseCurrency) throws IOException {
        if(API_KEY == null || API_KEY.isEmpty()){
            throw new IllegalArgumentException("API_KEY is not successfully configured as an environment variable");
        }

        String apiUrl = String.format(API_ENDPOINT, API_KEY);

        if(baseCurrency != null && !baseCurrency.isEmpty()){
            apiUrl += "/" + baseCurrency;
        }

        URL url = new URL(apiUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        return connection;
    }
}
