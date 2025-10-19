# Changelog

All notable changes to this project will be documented in this file.

## [1.3.0] - 2025-10-19

### Added
- **API Key Popup Dialog** 🔐
  - User-friendly popup to enter OpenAI API key
  - Settings button to change API key anytime
  - Secure storage in SharedPreferences
  - Professional Material Design dialog
  - Link to get OpenAI API key
  - Pre-fills existing key for editing

### Changed
- **Removed Hardcoded API Keys** 
  - No more hardcoded keys in source code
  - No longer using local_api_keys.xml or api_keys.xml
  - OpenAIService now gets key from SharedPreferences
  - More secure and production-ready approach

### Security
- API keys now stored in Android SharedPreferences
- No sensitive data in source code
- Easy to rotate/change keys
- User controls their own API key

### Benefits
- ✅ Better security (no hardcoded keys)
- ✅ User-friendly (popup dialog)
- ✅ Easy to change keys (Settings button)
- ✅ Production-ready approach
- ✅ Clean source code

## [1.2.0] - 2025-10-19

### Added
- **Intelligent News Combination Feature** 🧠
  - App now fetches up to 5 recent articles from all sources
  - OpenAI analyzes articles to detect related topics
  - Automatically combines related news into ONE comprehensive case study
  - Multi-source citations (shows all sources used)
  - AI-powered topic detection and synthesis

### Changed
- Enhanced `OpenAIService` with `generateCombinedCaseStudy()` method
- Updated `NewsScraperService` to fetch top 5 articles instead of 1
- Simplified `MainActivity` callback logic for single case study generation
- Improved case study quality with multi-source perspectives

### Benefits
- Richer content from multiple sources
- Better context with combined insights
- More comprehensive analysis
- Proper attribution of all sources
- Same API efficiency (1 call per refresh)

## [1.1.0] - 2025-10-19

### Changed
- **Updated app to show only 1 case study instead of 10**
  - Modified `NewsScraperService.java` to fetch only the most recent article
  - Updated `MainActivity.java` messages to reflect single case study
  - Changed `strings.xml` to use singular forms
  - Updated README with new usage instructions
  - App now generates one comprehensive case study from the latest tech news

### Why This Change?
- Faster load times (generates 1 case study instead of 10)
- More focused user experience
- Reduced API usage and costs
- Quicker refresh for latest news

## [1.0.1] - 2025-10-19

### Fixed
- **Java 21 Compatibility Issue**
  - Upgraded Gradle from 8.0 to 8.5 for Java 21 support
  - Updated Android Gradle Plugin from 8.1.0 to 8.2.0
  - Fixed "Unsupported class file major version 65" error

### Added
- Comprehensive troubleshooting guide
- Android Studio build instructions
- Detailed documentation

## [1.0.0] - 2025-10-19

### Added
- Initial release of Android News Case Study App
- Multi-source news aggregation (TechCrunch, HackerNews, ProductHunt, Reddit, Google News)
- OpenAI GPT-3.5 integration for Harvard-style case study generation
- On-screen debug logger
- Material Design UI with RecyclerView
- Secure API key configuration system
- Comprehensive documentation (README, SETUP_GUIDE, PROJECT_SUMMARY, API_KEY_SETUP)

### Security
- Removed hardcoded API keys from source code
- Implemented secure configuration file system
- Added .gitignore protection for sensitive files
- Cleaned git history of exposed secrets

---

## Current Features

### Core Functionality
- ✅ Fetches latest tech news from 5 sources
- ✅ Generates 1 Harvard-style case study using OpenAI
- ✅ Real-time debug logging
- ✅ Auto-refresh capability
- ✅ Material Design UI

### Case Study Format
- Title
- Executive Summary
- Background
- Problem Statement
- Analysis
- Alternatives/Options
- Recommendations
- Conclusion
- Source Citation

### Technical Stack
- Java
- Android SDK (Min API 24, Target API 34)
- Gradle 8.5
- Android Gradle Plugin 8.2.0
- OkHttp, Retrofit, Gson, Jsoup
- OpenAI API (GPT-3.5 Turbo)

---

## Upgrade Notes

### From 1.0.0 to 1.1.0

**No breaking changes.** Simply update your code:

```bash
cd C:\Users\91956\AndroidNewsApp
git pull
```

Then in Android Studio:
```
File → Sync Project with Gradle Files
Build → Make Project
```

**What to Expect:**
- App now generates 1 case study instead of 10
- Faster initial load (15-30 seconds → 5-10 seconds)
- Reduced OpenAI API usage (10 calls → 1 call per refresh)
- More focused user experience

---

## Future Roadmap

### Planned Features
- [ ] User preference to select number of case studies (1-10)
- [ ] Save/bookmark favorite case studies
- [ ] Share case studies via social media
- [ ] Export case studies as PDF
- [ ] Dark mode support
- [ ] Swipe-to-refresh gesture
- [ ] Offline caching of case studies
- [ ] Search/filter by news source
- [ ] Notification for new case studies

### Under Consideration
- [ ] Multiple AI models (GPT-4, Claude, etc.)
- [ ] Custom case study templates
- [ ] Collaborative features
- [ ] Analytics dashboard
- [ ] Multi-language support

---

*For detailed information, see [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)*

