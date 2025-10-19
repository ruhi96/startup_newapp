package com.ruhi.newsapp.services;

import android.util.Log;

import com.ruhi.newsapp.models.NewsArticle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsScraperService {
    private static final String TAG = "NewsScraperService";

    public interface NewsCallback {
        void onSuccess(List<NewsArticle> articles);
        void onError(String error);
        void onProgress(String message);
    }

    public static void fetchAllNews(NewsCallback callback) {
        new Thread(() -> {
            List<NewsArticle> allArticles = new ArrayList<>();
            
            try {
                callback.onProgress("Fetching from HackerNews...");
                allArticles.addAll(fetchHackerNews());
                
                callback.onProgress("Fetching from TechCrunch...");
                allArticles.addAll(fetchTechCrunch());
                
                callback.onProgress("Fetching from ProductHunt...");
                allArticles.addAll(fetchProductHunt());
                
                callback.onProgress("Fetching from Reddit...");
                allArticles.addAll(fetchReddit());
                
                callback.onProgress("Fetching from Google News...");
                allArticles.addAll(fetchGoogleNews());
                
                // Sort by timestamp and get top 10
                allArticles.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
                
                List<NewsArticle> top10 = allArticles.subList(0, Math.min(10, allArticles.size()));
                callback.onSuccess(top10);
                
            } catch (Exception e) {
                Log.e(TAG, "Error fetching news", e);
                callback.onError(e.getMessage());
            }
        }).start();
    }

    private static List<NewsArticle> fetchHackerNews() {
        List<NewsArticle> articles = new ArrayList<>();
        try {
            // Fetch top stories from HackerNews API
            String topStoriesUrl = "https://hacker-news.firebaseio.com/v0/topstories.json";
            String response = makeHttpRequest(topStoriesUrl);
            JSONArray topStories = new JSONArray(response);
            
            // Get first 3 stories
            for (int i = 0; i < Math.min(3, topStories.length()); i++) {
                int storyId = topStories.getInt(i);
                String storyUrl = "https://hacker-news.firebaseio.com/v0/item/" + storyId + ".json";
                String storyResponse = makeHttpRequest(storyUrl);
                JSONObject story = new JSONObject(storyResponse);
                
                String title = story.optString("title", "");
                String url = story.optString("url", "https://news.ycombinator.com/item?id=" + storyId);
                String text = story.optString("text", "");
                
                articles.add(new NewsArticle(title, url, "HackerNews", text, text));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching HackerNews", e);
        }
        return articles;
    }

    private static List<NewsArticle> fetchTechCrunch() {
        List<NewsArticle> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://techcrunch.com/")
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();
            
            Elements articleElements = doc.select("article, .post-block, h2.post-block__title");
            
            for (int i = 0; i < Math.min(2, articleElements.size()); i++) {
                Element article = articleElements.get(i);
                Element titleElement = article.selectFirst("h2, a.post-block__title__link, .post-block__title a");
                
                if (titleElement != null) {
                    String title = titleElement.text();
                    String url = titleElement.absUrl("href");
                    if (url.isEmpty()) {
                        Element linkElement = article.selectFirst("a");
                        if (linkElement != null) {
                            url = linkElement.absUrl("href");
                        }
                    }
                    
                    Element descElement = article.selectFirst("p, .post-block__content");
                    String description = descElement != null ? descElement.text() : "";
                    
                    if (!title.isEmpty() && !url.isEmpty()) {
                        articles.add(new NewsArticle(title, url, "TechCrunch", description, description));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching TechCrunch", e);
        }
        return articles;
    }

    private static List<NewsArticle> fetchProductHunt() {
        List<NewsArticle> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://www.producthunt.com/")
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();
            
            Elements postElements = doc.select("[data-test='post-item'], article, .styles_item__Xrx7U");
            
            for (int i = 0; i < Math.min(2, postElements.size()); i++) {
                Element post = postElements.get(i);
                Element titleElement = post.selectFirst("h3, strong, a");
                
                if (titleElement != null) {
                    String title = titleElement.text();
                    String url = "https://www.producthunt.com" + post.selectFirst("a").attr("href");
                    Element descElement = post.selectFirst("p, span");
                    String description = descElement != null ? descElement.text() : "";
                    
                    if (!title.isEmpty()) {
                        articles.add(new NewsArticle(title, url, "ProductHunt", description, description));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching ProductHunt", e);
        }
        return articles;
    }

    private static List<NewsArticle> fetchReddit() {
        List<NewsArticle> articles = new ArrayList<>();
        try {
            // Use Reddit JSON API
            String redditUrl = "https://www.reddit.com/r/technology/.json?limit=3";
            String response = makeHttpRequest(redditUrl);
            JSONObject json = new JSONObject(response);
            JSONArray children = json.getJSONObject("data").getJSONArray("children");
            
            for (int i = 0; i < children.length(); i++) {
                JSONObject post = children.getJSONObject(i).getJSONObject("data");
                String title = post.optString("title", "");
                String url = post.optString("url", "");
                String selftext = post.optString("selftext", "");
                
                articles.add(new NewsArticle(title, url, "Reddit", selftext, selftext));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching Reddit", e);
        }
        return articles;
    }

    private static List<NewsArticle> fetchGoogleNews() {
        List<NewsArticle> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://news.google.com/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRGRqTVhZU0FtVnVHZ0pWVXlnQVAB")
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();
            
            Elements articleElements = doc.select("article, .NiLAwe");
            
            for (int i = 0; i < Math.min(2, articleElements.size()); i++) {
                Element article = articleElements.get(i);
                Element titleElement = article.selectFirst("h3, h4, a");
                
                if (titleElement != null) {
                    String title = titleElement.text();
                    String relativeUrl = titleElement.selectFirst("a") != null ? 
                            titleElement.selectFirst("a").attr("href") : "";
                    String url = relativeUrl.startsWith("./") ? 
                            "https://news.google.com" + relativeUrl.substring(1) : 
                            "https://news.google.com";
                    
                    if (!title.isEmpty()) {
                        articles.add(new NewsArticle(title, url, "Google News", "", ""));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching Google News", e);
        }
        return articles;
    }

    private static String makeHttpRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        
        return response.toString();
    }
}

