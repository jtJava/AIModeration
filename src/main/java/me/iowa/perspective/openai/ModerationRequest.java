package me.iowa.perspective.openai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.iowa.perspective.Perspective;
import me.iowa.perspective.openai.json.ModerationResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class ModerationRequest {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/moderations";
    private final String API_SECRET_KEY = Perspective.getInstance().getMiscConfig().getOpenAiKey();
    private static final Gson GSON = new GsonBuilder().create();

    private final String input;

    public ModerationResponse send() {
        // Serialize request object to JSON
        String jsonRequest = GSON.toJson(this);

        // Send API request
        try {
            URL url = new URL(API_ENDPOINT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + API_SECRET_KEY);
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonRequest);
            writer.flush();

            if (connection.getResponseCode() != 200) {
                return null;
            }

            // Read API response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonResponse = reader.lines().collect(Collectors.joining());
            reader.close();

            // Deserialize response JSON to object
            return GSON.fromJson(jsonResponse, ModerationResponse.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}