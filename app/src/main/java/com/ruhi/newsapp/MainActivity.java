package com.ruhi.newsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

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
    private static final String PREFS_NAME = "NewsAppPrefs";
    private static final String KEY_API_KEY = "openai_api_key";
    
    private RecyclerView recyclerView;
    private CaseStudyAdapter adapter;
    private ProgressBar progressBar;
    private Button btnRefresh;
    private Button btnToggleDebug;
    private Button btnSettings;
    private ScrollView debugView;
    private TextView tvDebug;
    
    private SharedPreferences sharedPreferences;
    private boolean debugVisible = false;
    private StringBuilder debugLog = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        initViews();
        setupRecyclerView();
        setupListeners();
        
        logDebug("App started");
        
        // Check if API key exists, if not show dialog
        checkAndRequestApiKey();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnToggleDebug = findViewById(R.id.btnToggleDebug);
        btnSettings = findViewById(R.id.btnSettings);
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
            if (hasApiKey()) {
                loadCaseStudies();
            } else {
                showApiKeyDialog();
            }
        });
        
        btnToggleDebug.setOnClickListener(v -> {
            debugVisible = !debugVisible;
            debugView.setVisibility(debugVisible ? View.VISIBLE : View.GONE);
            logDebug("Debug view " + (debugVisible ? "shown" : "hidden"));
        });
        
        btnSettings.setOnClickListener(v -> {
            logDebug("Settings button clicked");
            showApiKeyDialog();
        });
    }

    private void loadCaseStudies() {
        logDebug("Starting to load case study...");
        progressBar.setVisibility(View.VISIBLE);
        btnRefresh.setEnabled(false);
        
        NewsScraperService.fetchAllNews(new NewsScraperService.NewsCallback() {
            @Override
            public void onSuccess(List<NewsArticle> articles) {
                logDebug("Fetched " + articles.size() + " news article(s)");
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
        logDebug("Analyzing " + articles.size() + " articles for related topics...");
        logDebug("Generating combined case study with OpenAI...");
        
        // Use the new combined method that intelligently merges related articles
        OpenAIService.generateCombinedCaseStudy(this, articles, new OpenAIService.CaseStudyCallback() {
            @Override
            public void onSuccess(CaseStudy caseStudy) {
                logDebug("Case study generated successfully!");
                runOnUiThread(() -> {
                    List<CaseStudy> caseStudies = new ArrayList<>();
                    caseStudies.add(caseStudy);
                    adapter.setCaseStudies(caseStudies);
                    progressBar.setVisibility(View.GONE);
                    btnRefresh.setEnabled(true);
                    logDebug("Case study loaded and displayed!");
                    Toast.makeText(MainActivity.this, "Case study loaded!", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                logDebug("ERROR generating case study: " + error);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRefresh.setEnabled(true);
                    logDebug("Failed to load case study");
                    Toast.makeText(MainActivity.this, 
                            "Failed to load case study: " + error, 
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void checkAndRequestApiKey() {
        if (!hasApiKey()) {
            logDebug("No API key found, showing dialog...");
            showApiKeyDialog();
        } else {
            logDebug("API key found, loading case studies...");
            loadCaseStudies();
        }
    }
    
    private boolean hasApiKey() {
        String apiKey = sharedPreferences.getString(KEY_API_KEY, "");
        return apiKey != null && !apiKey.trim().isEmpty();
    }
    
    private void showApiKeyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_api_key, null);
        TextInputEditText etApiKey = dialogView.findViewById(R.id.etApiKey);
        
        // Pre-fill with existing key if available
        String existingKey = sharedPreferences.getString(KEY_API_KEY, "");
        if (!existingKey.isEmpty()) {
            etApiKey.setText(existingKey);
        }
        
        builder.setView(dialogView)
                .setTitle("OpenAI API Key")
                .setPositiveButton("Save", (dialog, which) -> {
                    String apiKey = etApiKey.getText().toString().trim();
                    if (!apiKey.isEmpty()) {
                        saveApiKey(apiKey);
                        logDebug("API key saved successfully");
                        Toast.makeText(this, "API key saved!", Toast.LENGTH_SHORT).show();
                        
                        // Load case studies if we just saved a new key
                        if (adapter.getItemCount() == 0) {
                            loadCaseStudies();
                        }
                    } else {
                        Toast.makeText(this, "API key cannot be empty", Toast.LENGTH_SHORT).show();
                        logDebug("API key cannot be empty");
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    if (!hasApiKey()) {
                        logDebug("No API key provided, app cannot function");
                        Toast.makeText(this, "API key required to generate case studies", 
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setCancelable(false);
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    private void saveApiKey(String apiKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_API_KEY, apiKey);
        editor.apply();
    }
    
    public String getApiKey() {
        return sharedPreferences.getString(KEY_API_KEY, "");
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

