package app.services;

import app.dtos.QuoteDTO;
import app.dtos.QuoteResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class QuoteService {
    private final String apiKey;

    public QuoteService(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<QuoteDTO> fetchQuotes(int count) throws Exception {
        String urlString = "https://api.api-ninjas.com/v1/quotes?count=" + count;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-Api-Key", apiKey);

        InputStream responseStream = conn.getInputStream();
        String jsonString;
        try (Scanner scanner = new Scanner(responseStream)) {
            scanner.useDelimiter("\\A");
            jsonString = scanner.hasNext() ? scanner.next() : "";
        }

        // Use ObjectMapper and wrapper DTO to handle single or multiple quotes
        ObjectMapper mapper = new ObjectMapper();
        if (jsonString.trim().startsWith("{")) {
            jsonString = "{ \"quotes\": [" + jsonString + "] }";
        } else if (jsonString.trim().startsWith("[")) {
            jsonString = "{ \"quotes\": " + jsonString + " }";
        }

        QuoteResponseDTO response = mapper.readValue(jsonString, QuoteResponseDTO.class);
        return response.getQuotes();
    }
}
