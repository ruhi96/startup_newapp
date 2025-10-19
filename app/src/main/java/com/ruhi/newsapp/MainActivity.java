package com.ruhi.newsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruhi.newsapp.adapters.CaseStudyAdapter;
import com.ruhi.newsapp.models.CaseStudy;
import com.ruhi.newsapp.models.NewsArticle;
import com.ruhi.newsapp.services.NewsScraperService;
import com.ruhi.newsapp.services.OpenAIService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private RecyclerView recyclerView;
    private CaseStudyAdapter adapter;
    private ProgressBar progressBar;
    private Button btnRefresh;
    private Button btnToggleDebug;
    private ScrollView debugView;
    private TextView tvDebug;
    
    private boolean debugVisible = false;
    private StringBuilder debugLog = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupRecyclerView();
        setupListeners();
        
        logDebug("App started");
        loadCaseStudies();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnToggleDebug = findViewById(R.id.btnToggleDebug);
        debugView = findViewById(R.id.debugView);
        tvDebug = findViewById(R.id.tvDebug);
    }

    private void setupRecyclerView() {
        adapter = new CaseStudyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnRefresh.setOnClickListener(v -> {
            logDebug("Refresh button clicked");
            loadCaseStudies();
        });
        
        btnToggleDebug.setOnClickListener(v -> {
            debugVisible = !debugVisible;
            debugView.setVisibility(debugVisible ? View.VISIBLE : View.GONE);
            logDebug("Debug view " + (debugVisible ? "shown" : "hidden"));
        });
    }

    private void loadCaseStudies() {
        logDebug("Starting to load case studies...");
        progressBar.setVisibility(View.VISIBLE);
        btnRefresh.setEnabled(false);
        
        NewsScraperService.fetchAllNews(new NewsScraperService.NewsCallback() {
            @Override
            public void onSuccess(List<NewsArticle> articles) {
                logDebug("Fetched " + articles.size() + " news articles");
                generateCaseStudies(articles);
            }

            @Override
            public void onError(String error) {
                logDebug("ERROR: " + error);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRefresh.setEnabled(true);
                    Toast.makeText(MainActivity.this, 
                            "Error: " + error, Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onProgress(String message) {
                logDebug(message);
                runOnUiThread(() -> 
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void generateCaseStudies(List<NewsArticle> articles) {
        logDebug("Generating case studies with OpenAI...");
        
        List<CaseStudy> caseStudies = new ArrayList<>();
        AtomicInteger completedCount = new AtomicInteger(0);
        final int totalArticles = articles.size();
        
        for (NewsArticle article : articles) {
            logDebug("Processing: " + article.getTitle());
            
            OpenAIService.generateCaseStudy(this, article, new OpenAIService.CaseStudyCallback() {
                @Override
                public void onSuccess(CaseStudy caseStudy) {
                    synchronized (caseStudies) {
                        caseStudies.add(caseStudy);
                        int completed = completedCount.incrementAndGet();
                        logDebug("Generated case study " + completed + "/" + totalArticles);
                        
                        if (completed == totalArticles) {
                            runOnUiThread(() -> {
                                adapter.setCaseStudies(caseStudies);
                                progressBar.setVisibility(View.GONE);
                                btnRefresh.setEnabled(true);
                                logDebug("All case studies loaded successfully!");
                                Toast.makeText(MainActivity.this, 
                                        "Loaded " + caseStudies.size() + " case studies", 
                                        Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    synchronized (caseStudies) {
                        int completed = completedCount.incrementAndGet();
                        logDebug("ERROR generating case study " + completed + "/" + 
                                totalArticles + ": " + error);
                        
                        if (completed == totalArticles) {
                            runOnUiThread(() -> {
                                adapter.setCaseStudies(caseStudies);
                                progressBar.setVisibility(View.GONE);
                                btnRefresh.setEnabled(true);
                                logDebug("Finished with " + caseStudies.size() + 
                                        " successful case studies");
                                Toast.makeText(MainActivity.this, 
                                        "Loaded " + caseStudies.size() + " case studies", 
                                        Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }
            });
        }
    }

    private void logDebug(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        String logMessage = "[" + timestamp + "] " + message + "\n";
        
        debugLog.append(logMessage);
        Log.d(TAG, message);
        
        runOnUiThread(() -> {
            tvDebug.setText(debugLog.toString());
            // Auto-scroll to bottom
            debugView.post(() -> debugView.fullScroll(View.FOCUS_DOWN));
        });
    }
}

