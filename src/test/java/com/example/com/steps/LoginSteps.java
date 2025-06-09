package com.example.com.steps;


import com.example.com.interactions.BaseInteractions;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import okhttp3.*;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.IOException;


public class LoginSteps extends BaseInteractions{
    private static final String API_KEY = "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";
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @Given("^I click (.+)$")
    public void click(String target) {
        String step = "click " + target;
        System.out.println("Step: " + step);
        String xpath = getXpath(step);
        clickOn(driver.findElement(By.xpath(xpath)));
    }

    @Given("^I see (.+)$")
    public void validate(String target) {
        String step = "validate " + target;
        System.out.println("Step: " + step);
        String xpath = getXpath(step);
        Assert.assertTrue("Element not found with XPath: " + xpath, isPresent(driver.findElement(By.xpath(xpath))));
    }


    private String getXpath(String stepString) {
        final int maxRetries = 10;
        int waitTimeMillis = 2000;
        int attempt = 0;
        OkHttpClient client = new OkHttpClient();
        while (attempt < maxRetries) {
            try {
                String requestBodyJson = buildRequestBody(stepString, driver.getPageSource());
                Request request = new Request.Builder()
                        .url(OPENAI_URL)
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .post(RequestBody.create(requestBodyJson, MediaType.parse("application/json")))
                        .build();
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseBodyString = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(responseBodyString);

                String contentString = responseJson
                        .path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText();
                JsonNode innerJson = mapper.readTree(contentString);

                String xpath = innerJson.path("xpath").asText();
                System.out.println("xpath: " + xpath);
                return xpath;

            } catch (IOException e) {
                attempt++;
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                waitTimeMillis *= 2;
                if (attempt >= maxRetries) {
                    throw new RuntimeException("Failed to get XPath after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(waitTimeMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted during retry wait", ie);
                }
            }
        }
        throw new RuntimeException("Unexpected error in getXpathWithRetry()");
    }

    private String buildRequestBody(String testStep, String dom) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Compose JSON as per OpenAI API structure
        String promptText =
                "This is my test step: "+ testStep + "\n"+
                "This is my DOM: " + dom + "\n"
                        + "Can you please analyse the DOM and return the XPath of the most likely web element in this format:\n"
                        + "{\n"
                        + "  \"xpath\": string\n"
                        + "}\n\n"+
                "Important:\n" +
                        "- The xpath must be unique for that element\n" +
                        "- Be careful about upper and lower case\n" +
                        "- Do not add explanations, newlines, or backticks.\n" +
                        "- Do not wrap the JSON in code blocks.\n" +
                        "- Only output the JSON object.";
        // Construct request body object
        var requestBody = mapper.createObjectNode();
        requestBody.put("model", "gpt-4-turbo");

        // System message
        var systemMessage = mapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are an AI assistant that helps extract XPaths from HTML DOM based on test steps.");

        // User message
        var userMessage = mapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", promptText);

        // Add messages array
        requestBody.putArray("messages").add(systemMessage).add(userMessage);

        requestBody.put("max_tokens", 300);

        return mapper.writeValueAsString(requestBody);
    }
}
