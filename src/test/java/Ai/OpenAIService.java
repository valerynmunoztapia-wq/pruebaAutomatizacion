package Ai;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import  java.util.*;

public class OpenAIService {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final String apiKey =
            System.getenv("OPENAI_API_KEY");

    private final OkHttpClient client =
            new OkHttpClient();

    private final ObjectMapper mapper =
            new ObjectMapper();

    public String generarNuevoXpath(
            String locatorOriginal,
            String html
    ) throws Exception {

        String prompt = """
        Eres experto en Selenium.

        El siguiente locator falló:

        %s

        HTML actual:

        %s

        Genera SOLO un xpath robusto.
        Sin explicaciones.
        """.formatted(locatorOriginal, html);

        Map<String, Object> requestMap =
                new HashMap<>();

        requestMap.put("model", "gpt-4.1-mini");

        List<Map<String, String>> messages =
                new ArrayList<>();

        messages.add(Map.of(
                "role", "user",
                "content", prompt
        ));

        requestMap.put("messages", messages);

        String body =
                mapper.writeValueAsString(requestMap);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader(
                        "Authorization",
                        "Bearer " + apiKey
                )
                .addHeader(
                        "Content-Type",
                        "application/json"
                )
                .post(
                        RequestBody.create(
                                body,
                                MediaType.parse(
                                        "application/json"
                                )
                        )
                )
                .build();

        Response response =
                client.newCall(request).execute();

        String responseBody =
                response.body().string();

        Map result =
                mapper.readValue(responseBody, Map.class);

        List choices =
                (List) result.get("choices");

        Map choice = (Map) choices.get(0);

        Map message =
                (Map) choice.get("message");

        return message
                .get("content")
                .toString()
                .trim();
    }
}
