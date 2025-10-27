package app.services;

import app.dtos.QuoteDTO;
import app.dtos.QuoteResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuoteService {
    private final String apiKey;

    public QuoteService(String apiKey) {
        this.apiKey = apiKey;
    }


        public List<QuoteDTO> fetchQuotes ( int count) throws Exception {
            List<QuoteDTO> allQuotes = new ArrayList<>();

            // 🧠 API Ninjas returns ONE quote per call, so we make multiple requests
            for (int i = 0; i < count; i++) {
                String urlString = "https://api.api-ninjas.com/v1/quotes";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-Api-Key", apiKey);

                try (InputStream responseStream = conn.getInputStream();
                     Scanner scanner = new Scanner(responseStream)) {
                    scanner.useDelimiter("\\A");
                    String jsonString = scanner.hasNext() ? scanner.next() : "";


                    if (jsonString.trim().startsWith("{")) {
                        jsonString = "{ \"quotes\": [" + jsonString + "] }";
                    } else if (jsonString.trim().startsWith("[")) {
                        jsonString = "{ \"quotes\": " + jsonString + " }";
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    QuoteResponseDTO response = mapper.readValue(jsonString, QuoteResponseDTO.class);
                    allQuotes.addAll(response.getQuotes());
                }

                conn.disconnect();
            }

            return allQuotes;
        }
    }
