# Android News Case Study App

An Android application that fetches the latest tech news from multiple sources and generates a Harvard-style case study using OpenAI's GPT technology.

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

2. **Configure OpenAI API Key** (IMPORTANT):
   - See [API_KEY_SETUP.md](API_KEY_SETUP.md) for detailed instructions
   - Quick setup: Copy `local_api_keys.xml` to project root and add your API key

3. Open the project in Android Studio

4. Sync Gradle files

5. Run the app on an emulator or physical device

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
2. The app automatically fetches the most recent tech news and generates a case study
3. Click "Refresh" to generate a new case study from the latest news
4. Toggle "Debug" view to see real-time operation logs
5. Scroll through the case study sections displayed in a card

## License

This project is for educational purposes.

## Author

Ruhi

