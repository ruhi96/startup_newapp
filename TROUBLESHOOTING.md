# Troubleshooting Guide

## Common Build Issues and Solutions

### 1. ✅ FIXED: "Unsupported class file major version 65"

**Problem:**
```
FAILURE: Build failed with an exception.
Unsupported class file major version 65
```

**Cause:**
- Java 21 (class version 65) is installed on your system
- Gradle 8.0 doesn't fully support Java 21

**Solution (Applied):**
✅ **Already Fixed!** Upgraded to Gradle 8.5 which supports Java 21.

The fix has been applied and pushed to GitHub. If you still see this error:
1. In Android Studio: File → Invalidate Caches → Restart
2. Or manually run: `./gradlew --stop` then sync again

---

### 2. Gradle Sync Issues

**Problem:** Gradle sync fails or takes too long

**Solutions:**
```bash
# Clean Gradle cache
cd C:\Users\91956\AndroidNewsApp
rmdir /s /q .gradle
rmdir /s /q build
rmdir /s /q app\build

# Then in Android Studio:
# File → Invalidate Caches → Restart
```

---

### 3. API Key Not Configured

**Problem:** 
```
API key not configured! Please add your OpenAI API key
```

**Solution:**
1. Check that `local_api_keys.xml` exists in project root: `C:\Users\91956\AndroidNewsApp\local_api_keys.xml`
2. Verify the file contains your OpenAI API key
3. Or edit `app/src/main/res/values/api_keys.xml` and add your key there

See [API_KEY_SETUP.md](API_KEY_SETUP.md) for detailed instructions.

---

### 4. Network/Internet Errors

**Problem:** App crashes or shows "Network Error"

**Solutions:**
- Check internet connection
- Disable VPN/Proxy if causing issues
- Verify OpenAI API key is valid and has credits
- Check if firewall is blocking connections

**Test your internet:**
```bash
ping news.ycombinator.com
ping api.openai.com
```

---

### 5. EmulatorMain Errors

**Problem:** Emulator won't start or crashes

**Solutions:**
1. **In Android Studio:**
   - Tools → Device Manager → Delete emulator and create new one
   - Use a different API level (try API 30 or 33)

2. **System Settings:**
   - Enable Virtualization in BIOS
   - Check Hyper-V is enabled (Windows)
   - Restart computer

---

### 6. Missing SDK or Build Tools

**Problem:** 
```
SDK location not found
Android SDK not found
```

**Solution:**
1. Open Android Studio
2. File → Project Structure → SDK Location
3. Set Android SDK location (usually: `C:\Users\<username>\AppData\Local\Android\Sdk`)
4. Tools → SDK Manager → Install required SDK and build tools

---

### 7. "Could not resolve" Dependency Errors

**Problem:**
```
Could not resolve com.squareup.okhttp3:okhttp:4.11.0
```

**Solutions:**
1. Check internet connection
2. In Android Studio: File → Settings → Build → Gradle
3. Set Gradle to use project's gradle wrapper
4. Sync again

**Or manually:**
```bash
cd C:\Users\91956\AndroidNewsApp
gradlew clean build --refresh-dependencies
```

---

### 8. Java Version Issues

**Check your Java version:**
```bash
java -version
```

**Required:**
- Java 17 or higher (Java 21 supported with Gradle 8.5+)

**If using wrong Java version:**
1. In Android Studio: File → Settings → Build, Execution, Deployment → Build Tools → Gradle
2. Set "Gradle JDK" to Java 17 or 21

---

### 9. Out of Memory Errors

**Problem:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Solution:**
Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

---

### 10. App Crashes on Startup

**Check Logcat:**
1. In Android Studio: View → Tool Windows → Logcat
2. Look for red error messages
3. Common causes:
   - Missing API key
   - Network permission denied
   - Missing dependencies

**Common fixes:**
- Clean and rebuild: Build → Clean Project, then Build → Rebuild Project
- Uninstall app from device/emulator and reinstall
- Check AndroidManifest.xml has INTERNET permission

---

## Build Commands

### Clean Build
```bash
cd C:\Users\91956\AndroidNewsApp
gradlew clean
```

### Build APK
```bash
gradlew assembleDebug
```

### Install on Device
```bash
gradlew installDebug
```

### Check Dependencies
```bash
gradlew app:dependencies
```

---

## Getting Help

### Check Logs
1. **Android Studio Logcat**: View → Tool Windows → Logcat
2. **Gradle Build Output**: View → Tool Windows → Build
3. **Event Log**: View → Tool Windows → Event Log

### Debug Mode
Enable debug view in the app:
- Tap "Toggle Debug" button
- View real-time operation logs

### Still Having Issues?

1. Check the debug log in the app
2. Review Android Studio's Logcat
3. Verify all steps in [SETUP_GUIDE.md](SETUP_GUIDE.md)
4. Try building from command line: `gradlew assembleDebug`
5. Check GitHub Issues: https://github.com/ruhi96/startup_newapp/issues

---

## System Requirements

### Minimum:
- **OS:** Windows 10/11, macOS 10.14+, Linux
- **RAM:** 8 GB (16 GB recommended)
- **Disk Space:** 4 GB for Android Studio + 2 GB for Android SDK
- **Java:** JDK 17 or higher
- **Android Studio:** Arctic Fox (2020.3.1) or newer
- **Internet:** Required for dependencies and API calls

### Recommended:
- **RAM:** 16 GB
- **Java:** JDK 21
- **Android Studio:** Latest stable version
- **Gradle:** 8.5+ (already configured)

---

## Quick Fixes Checklist

When build fails, try these in order:

- [ ] File → Invalidate Caches → Restart
- [ ] Build → Clean Project
- [ ] Build → Rebuild Project
- [ ] Delete `.gradle` and `build` folders
- [ ] Sync Project with Gradle Files
- [ ] Check internet connection
- [ ] Verify API key is configured
- [ ] Update Android Studio to latest version
- [ ] Check Java version (should be 17+)

---

## Version Compatibility Matrix

| Component | Version | Status |
|-----------|---------|--------|
| Gradle | 8.5 | ✅ Current |
| Android Gradle Plugin | 8.2.0 | ✅ Current |
| Compile SDK | 34 | ✅ Current |
| Min SDK | 24 (Android 7.0) | ✅ Supported |
| Target SDK | 34 (Android 14) | ✅ Current |
| Java | 17-21 | ✅ Supported |
| Kotlin (if added) | 1.9.0+ | ✅ Compatible |

---

*Last Updated: October 19, 2025*
*For more help, see [SETUP_GUIDE.md](SETUP_GUIDE.md) or [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)*

