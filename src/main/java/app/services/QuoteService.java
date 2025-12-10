package app.services;

import app.daos.QuoteDAO;
import app.dtos.QuoteDTO;
import app.dtos.QuoteResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuoteService {
    private final String apiKey;
    private static QuoteService instance;

    public QuoteService(String apiKey) {
        this.apiKey = apiKey;
    }


    public static QuoteService getInstance(String apiKey) {
        if (instance == null) {
            instance = new QuoteService(apiKey);
        }
        return instance;
    }

    public List<QuoteDTO> fetchQuotes(int count) throws Exception {
        List<QuoteDTO> allQuotes = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < count; i++) {
            String urlString = "https://api.api-ninjas.com/v1/quotes";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Api-Key", apiKey);

            int status = conn.getResponseCode();
            System.out.println("API call #" + (i + 1) + " status: " + status);

            InputStream responseStream;
            if (status >= 200 && status < 300) {
                responseStream = conn.getInputStream();
            } else {
                // læs fejl og smid en exception så du kan se hvorfor
                InputStream errorStream = conn.getErrorStream();
                String errorBody = errorStream != null ? new String(errorStream.readAllBytes()) : "";
                throw new RuntimeException("API request failed. Status: " + status + ", body: " + errorBody);
            }

            String jsonString;
            try (Scanner scanner = new Scanner(responseStream)) {
                scanner.useDelimiter("\\A");
                jsonString = scanner.hasNext() ? scanner.next() : "";
            }

            if (jsonString.trim().startsWith("{")) {
                jsonString = "{ \"quotes\": [" + jsonString + "] }";
            } else if (jsonString.trim().startsWith("[")) {
                jsonString = "{ \"quotes\": " + jsonString + " }";
            }

            QuoteResponseDTO response = mapper.readValue(jsonString, QuoteResponseDTO.class);
            allQuotes.addAll(response.getQuotes());

            conn.disconnect();
        }

        System.out.println("Fetched total quotes: " + allQuotes.size());
        return allQuotes;
    }
}
