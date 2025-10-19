package com.ruhi.newsapp.services;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.ruhi.newsapp.R;
import com.ruhi.newsapp.models.CaseStudy;
import com.ruhi.newsapp.models.NewsArticle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OpenAIService {
    private static final String TAG = "OpenAIService";
    private static String API_KEY = null;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    
    private static void initApiKey(Context context) {
        if (API_KEY != null) return;
        
        try {
            // Try to load from local_api_keys.xml first (not committed to git)
            File localKeysFile = new File(context.getFilesDir().getParentFile().getParentFile().getParent(), "local_api_keys.xml");
            if (localKeysFile.exists()) {
                API_KEY = parseApiKeyFromFile(localKeysFile);
                Log.d(TAG, "Loaded API key from local_api_keys.xml");
                return;
            }
        } catch (Exception e) {
            Log.d(TAG, "Could not load from local_api_keys.xml: " + e.getMessage());
        }
        
        try {
            // Fall back to api_keys.xml from resources
            API_KEY = context.getString(R.string.openai_api_key);
            if (API_KEY.equals("YOUR_OPENAI_API_KEY_HERE")) {
                Log.e(TAG, "API key not configured! Please add your OpenAI API key to api_keys.xml");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to load API key", e);
            API_KEY = "YOUR_OPENAI_API_KEY_HERE";
        }
    }
    
    private static String parseApiKeyFromFile(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("openai_api_key")) {
                int start = line.indexOf(">") + 1;
                int end = line.lastIndexOf("<");
                if (start > 0 && end > start) {
                    reader.close();
                    return line.substring(start, end);
                }
            }
        }
        reader.close();
        throw new Exception("API key not found in file");
    }
    
    public interface CaseStudyCallback {
        void onSuccess(CaseStudy caseStudy);
        void onError(String error);
    }

    public static void generateCaseStudy(Context context, NewsArticle article, CaseStudyCallback callback) {
        new Thread(() -> {
            try {
                initApiKey(context);
                String prompt = buildPrompt(article);
                String response = callOpenAI(prompt);
                CaseStudy caseStudy = parseCaseStudy(response, article);
                callback.onSuccess(caseStudy);
            } catch (Exception e) {
                Log.e(TAG, "Error generating case study", e);
                callback.onError(e.getMessage());
            }
        }).start();
    }

    private static String buildPrompt(NewsArticle article) {
        return "You are a Harvard Business School case study writer. Based on the following news article, " +
                "create a comprehensive case study in Harvard format. Use simple, clear language.\n\n" +
                "Article Title: " + article.getTitle() + "\n" +
                "Source: " + article.getSource() + "\n" +
                "Content: " + (article.getContent() != null ? article.getContent() : article.getDescription()) + "\n\n" +
                "Please provide the case study in the following JSON format:\n" +
                "{\n" +
                "  \"title\": \"Clear, descriptive title\",\n" +
                "  \"executiveSummary\": \"Brief overview (2-3 sentences)\",\n" +
                "  \"background\": \"Context and history (3-4 sentences)\",\n" +
                "  \"problemStatement\": \"The main challenge or issue (2-3 sentences)\",\n" +
                "  \"analysis\": \"Detailed examination of the situation (4-5 sentences)\",\n" +
                "  \"alternatives\": \"Possible approaches or solutions (3-4 options)\",\n" +
                "  \"recommendations\": \"Suggested course of action (3-4 sentences)\",\n" +
                "  \"conclusion\": \"Summary and outlook (2-3 sentences)\"\n" +
                "}\n\n" +
                "Ensure all text is in simple language, suitable for general readers.";
    }

    private static String callOpenAI(String prompt) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messages.put(message);
        
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1500);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        Log.d(TAG, "OpenAI API Response Code: " + responseCode);

        BufferedReader br;
        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        br.close();

        if (responseCode != 200) {
            throw new Exception("OpenAI API Error: " + response.toString());
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray choices = jsonResponse.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject messageObj = firstChoice.getJSONObject("message");
        return messageObj.getString("content");
    }

    private static CaseStudy parseCaseStudy(String response, NewsArticle article) throws Exception {
        // Extract JSON from response (might be wrapped in markdown code blocks)
        String jsonStr = response.trim();
        if (jsonStr.startsWith("```json")) {
            jsonStr = jsonStr.substring(7);
        }
        if (jsonStr.startsWith("```")) {
            jsonStr = jsonStr.substring(3);
        }
        if (jsonStr.endsWith("```")) {
            jsonStr = jsonStr.substring(0, jsonStr.length() - 3);
        }
        jsonStr = jsonStr.trim();

        JSONObject json = new JSONObject(jsonStr);
        
        CaseStudy caseStudy = new CaseStudy();
        caseStudy.setTitle(json.optString("title", article.getTitle()));
        caseStudy.setExecutiveSummary(json.optString("executiveSummary", ""));
        caseStudy.setBackground(json.optString("background", ""));
        caseStudy.setProblemStatement(json.optString("problemStatement", ""));
        caseStudy.setAnalysis(json.optString("analysis", ""));
        caseStudy.setAlternatives(json.optString("alternatives", ""));
        caseStudy.setRecommendations(json.optString("recommendations", ""));
        caseStudy.setConclusion(json.optString("conclusion", ""));
        caseStudy.setSource("Source: " + article.getSource() + " - " + article.getUrl());
        caseStudy.setSourceUrl(article.getUrl());
        
        return caseStudy;
    }
}

