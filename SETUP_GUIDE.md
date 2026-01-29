# 🛠️ Setup Guide - Bloom Android App

## Prerequisites

### Required Software
- **Android Studio** - Hedgehog (2023.1.1) or later
- **JDK** - Java Development Kit 17 or higher
- **Android SDK** - API Level 24 (Android 7.0) minimum
- **Kotlin** - 1.9.20 or higher (bundled with Android Studio)

### Hardware Requirements
- **RAM** - Minimum 8GB (16GB recommended)
- **Storage** - At least 10GB free space
- **Processor** - Intel i5 or equivalent

## Step-by-Step Setup

### 1. Clone Repository

```bash
git clone https://github.com/rahul700raj/bloom-android.git
cd bloom-android
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Click "Open" or "Open an Existing Project"
3. Navigate to the cloned `bloom-android` folder
4. Click "OK"
5. Wait for Gradle sync to complete

### 3. Configure Backend API URL

Open `app/build.gradle` and update the API base URL:

```gradle
android {
    defaultConfig {
        // Change this to your backend API URL
        buildConfigField "String", "API_BASE_URL", '"https://your-backend-url.com/api/"'
    }
}
```

**Important:** Make sure to include the trailing slash `/api/`

### 4. Sync Gradle

1. Click "Sync Now" if prompted
2. Or go to File → Sync Project with Gradle Files
3. Wait for sync to complete

### 5. Setup Backend (Required)

The app requires the Bloom Backend API to function. Follow these steps:

1. Clone backend repository:
   ```bash
   git clone https://github.com/rahul700raj/bloom-backend.git
   ```

2. Follow backend setup instructions in its README

3. Deploy backend to Railway/Render/Heroku (see DEPLOYMENT.md in backend repo)

4. Get your backend URL and update in step 3 above

### 6. Run the App

#### On Emulator:
1. Open AVD Manager (Tools → Device Manager)
2. Create a new virtual device if needed
3. Select device with API 24 or higher
4. Click "Run" button or press Shift+F10

#### On Physical Device:
1. Enable Developer Options on your Android device
2. Enable USB Debugging
3. Connect device via USB
4. Select your device from the device dropdown
5. Click "Run"

## Project Configuration

### Gradle Files

#### Root `build.gradle`
```gradle
buildscript {
    ext.kotlin_version = "1.9.20"
    // ... dependencies
}
```

#### App `build.gradle`
```gradle
android {
    compileSdk 34
    
    defaultConfig {
        applicationId "com.bloom.app"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }
}
```

### Dependencies

All dependencies are already configured in `app/build.gradle`:
- AndroidX libraries
- Material Design 3
- Retrofit for networking
- Room for local database
- Glide for image loading
- Coroutines for async operations
- And more...

## Common Issues & Solutions

### Issue 1: Gradle Sync Failed

**Solution:**
```bash
# Clean and rebuild
./gradlew clean
./gradlew build

# Or in Android Studio:
Build → Clean Project
Build → Rebuild Project
```

### Issue 2: SDK Not Found

**Solution:**
1. Open SDK Manager (Tools → SDK Manager)
2. Install Android SDK Platform 34
3. Install Android SDK Build-Tools
4. Click "Apply"

### Issue 3: Kotlin Plugin Error

**Solution:**
1. File → Settings → Plugins
2. Search for "Kotlin"
3. Update Kotlin plugin
4. Restart Android Studio

### Issue 4: API Connection Failed

**Solution:**
1. Check backend is running
2. Verify API URL in build.gradle
3. Check internet permission in AndroidManifest.xml
4. For localhost, use `10.0.2.2` instead of `localhost` on emulator

### Issue 5: Build Variant Issues

**Solution:**
1. Build → Select Build Variant
2. Choose "debug" for development
3. Choose "release" for production builds

## Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Manual Testing Checklist
- [ ] Splash screen displays correctly
- [ ] Login/Register works
- [ ] Home screen loads categories and products
- [ ] Product search and filters work
- [ ] Product details display correctly
- [ ] Add to cart functionality
- [ ] Cart operations (add, update, remove)
- [ ] Checkout process
- [ ] Profile management
- [ ] Logout functionality

## Building Release APK

### Debug APK (for testing)
```bash
./gradlew assembleDebug
```
APK location: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for production)

1. Generate signing key:
```bash
keytool -genkey -v -keystore bloom-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias bloom
```

2. Add to `app/build.gradle`:
```gradle
android {
    signingConfigs {
        release {
            storeFile file("bloom-release-key.jks")
            storePassword "your_password"
            keyAlias "bloom"
            keyPassword "your_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

3. Build release APK:
```bash
./gradlew assembleRelease
```
APK location: `app/build/outputs/apk/release/app-release.apk`

## Environment Setup

### For Development
```gradle
buildConfigField "String", "API_BASE_URL", '"http://10.0.2.2:5000/api/"'
```

### For Production
```gradle
buildConfigField "String", "API_BASE_URL", '"https://your-production-url.com/api/"'
```

## IDE Configuration

### Recommended Android Studio Settings

1. **Code Style**
   - File → Settings → Editor → Code Style → Kotlin
   - Set from: Kotlin style guide

2. **Auto Import**
   - File → Settings → Editor → General → Auto Import
   - Enable "Add unambiguous imports on the fly"

3. **Logcat Colors**
   - File → Settings → Editor → Color Scheme → Android Logcat
   - Customize colors for better readability

## Useful Commands

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test

# Check dependencies
./gradlew dependencies

# List all tasks
./gradlew tasks
```

## Next Steps

1. ✅ Setup complete
2. 📱 Run the app
3. 🧪 Test all features
4. 🎨 Customize UI/branding
5. 🚀 Deploy backend
6. 📦 Build release APK
7. 🏪 Publish to Play Store

## Resources

- [Android Developer Guide](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Material Design 3](https://m3.material.io)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Glide Documentation](https://bumptech.github.io/glide/)

## Support

For issues or questions:
- Create an issue on GitHub
- Email: rm2778643@gmail.com
- Check backend repository for API issues

## License

MIT License - See LICENSE file for details