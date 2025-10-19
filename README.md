# Android News Case Study App

An Android application that fetches the latest tech news from multiple sources and generates Harvard-style case studies using OpenAI's GPT technology.

## Features

- **Multi-Source News Aggregation**: Fetches news from:
  - TechCrunch
  - Hacker News
  - Product Hunt
  - Reddit (r/technology)
  - Google News

- **AI-Powered Case Studies**: Uses OpenAI API to generate comprehensive case studies in Harvard format with:
  - Title
  - Executive Summary
  - Background
  - Problem Statement
  - Analysis
  - Alternatives/Options
  - Recommendations
  - Conclusion

- **On-Screen Debugger**: Real-time debug view to monitor app operations

- **Modern UI**: Material Design with RecyclerView and CardView components

## Requirements

- Android Studio Narwhal (4.0+)
- Android SDK 24+ (Android 7.0 Nougat or higher)
- OpenAI API Key
- Internet connection

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/ruhi96/startup_newapp.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on an emulator or physical device

## Architecture

- **Models**: Data classes for NewsArticle and CaseStudy
- **Services**: 
  - NewsScraperService: Fetches news from multiple sources
  - OpenAIService: Generates case studies using GPT
- **Adapters**: RecyclerView adapter for displaying case studies
- **MainActivity**: Orchestrates the entire app flow

## Technologies Used

- Java
- Android SDK
- OkHttp & Retrofit (Networking)
- Jsoup (HTML Parsing)
- Gson (JSON Parsing)
- OpenAI API
- Material Design Components
- RecyclerView & CardView

## Usage

1. Launch the app
2. Click "Refresh" to fetch latest news and generate case studies
3. Toggle "Debug" view to see real-time operation logs
4. Scroll through the case studies displayed in cards

## License

This project is for educational purposes.

## Author

Ruhi

