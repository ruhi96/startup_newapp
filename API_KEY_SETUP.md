# OpenAI API Key Setup

## Important Security Note

The OpenAI API key is **NOT** included in this repository for security reasons. You must add your own API key to run the app.

## Setup Instructions

### Method 1: Using local_api_keys.xml (Recommended for Development)

1. Copy the provided `local_api_keys.xml` file to the project root directory
2. Edit `local_api_keys.xml` and replace `YOUR_OPENAI_API_KEY_HERE` with your actual OpenAI API key
3. This file is already in `.gitignore` and won't be committed

Example `local_api_keys.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="openai_api_key">sk-proj-YOUR_ACTUAL_KEY_HERE</string>
</resources>
```

### Method 2: Using app resources (For Testing)

1. Open `app/src/main/res/values/api_keys.xml`
2. Replace `YOUR_OPENAI_API_KEY_HERE` with your actual OpenAI API key
3. **Warning**: Be careful not to commit this file to public repositories

## Getting Your OpenAI API Key

1. Go to [OpenAI Platform](https://platform.openai.com/)
2. Sign in or create an account
3. Navigate to API Keys section
4. Create a new API key
5. Copy the key (you won't be able to see it again!)

## Security Best Practices

- ✅ Never commit API keys to version control
- ✅ Use environment variables or secure key management for production
- ✅ Rotate API keys regularly
- ✅ Set usage limits in OpenAI dashboard
- ❌ Don't share your API keys
- ❌ Don't hardcode keys in source code

## Troubleshooting

If the app shows "API key not configured!" error:
1. Check that `local_api_keys.xml` exists in the project root
2. Verify the API key is correctly formatted
3. Ensure you're using a valid OpenAI API key
4. Check the debug log for specific error messages

## The Original API Key

The OpenAI API key that was provided with the initial request has been:
- Removed from all source code
- Extracted to separate configuration files
- Added to `.gitignore` to prevent future commits
- Stored in `local_api_keys.xml` (not tracked by git)

**For the developer:** Your original API key is saved in `local_api_keys.xml` in the project root. The app will automatically use it from there.

