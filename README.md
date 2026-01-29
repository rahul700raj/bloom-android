# 🌸 Bloom Android App

Modern e-commerce Android application built with Kotlin, MVVM architecture, and Material Design 3.

## ✨ Features

- **Modern UI/UX** - Material Design 3 with smooth animations
- **Authentication** - Secure login and registration with JWT
- **Product Catalog** - Browse products by categories and subcategories
- **Search & Filter** - Advanced search with price filters and sorting
- **Shopping Cart** - Add, update, and remove items from cart
- **Wishlist** - Save favorite products for later
- **Product Details** - Detailed product information with images
- **Order Management** - Place and track orders
- **User Profile** - Manage account and view order history
- **Offline Support** - Local caching with Room database
- **Responsive Design** - Works on all screen sizes

## 🏗️ Architecture

- **MVVM** - Model-View-ViewModel architecture pattern
- **Repository Pattern** - Clean separation of data sources
- **Coroutines** - Asynchronous programming
- **LiveData** - Observable data holder
- **ViewBinding** - Type-safe view access
- **Retrofit** - REST API communication
- **Room** - Local database
- **DataStore** - Preferences storage
- **Glide** - Image loading and caching

## 📱 Screenshots

[Add screenshots here]

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 24 or higher
- Kotlin 1.9.20 or higher

### Installation

1. Clone the repository
```bash
git clone https://github.com/rahul700raj/bloom-android.git
cd bloom-android
```

2. Open project in Android Studio

3. Update API base URL in `app/build.gradle`:
```gradle
buildConfigField "String", "API_BASE_URL", '"https://your-api-url.com/api/"'
```

4. Sync Gradle and build the project

5. Run on emulator or physical device

## 🔧 Configuration

### Backend API

This app requires the Bloom Backend API to be running. Get it here:
- Repository: [bloom-backend](https://github.com/rahul700raj/bloom-backend)
- Setup instructions in backend README

### API Endpoints

The app connects to these endpoints:
- `/api/auth/*` - Authentication
- `/api/categories` - Categories
- `/api/products` - Products
- `/api/users/*` - User operations
- `/api/orders` - Orders

## 📦 Dependencies

```gradle
// Core
- AndroidX Core KTX
- AppCompat
- Material Design 3
- ConstraintLayout

// Architecture
- Lifecycle (ViewModel, LiveData)
- Navigation Component
- Room Database
- DataStore

// Networking
- Retrofit
- OkHttp
- Gson

// Image Loading
- Glide

// UI
- ViewPager2
- SwipeRefreshLayout
- Lottie Animations

// Coroutines
- Kotlinx Coroutines
```

## 🎨 UI Components

- **Splash Screen** - Animated app launch
- **Authentication** - Login and registration screens
- **Home** - Featured products and categories
- **Categories** - Browse by category
- **Product List** - Grid/List view with filters
- **Product Detail** - Full product information
- **Cart** - Shopping cart management
- **Checkout** - Order placement
- **Profile** - User account management
- **Orders** - Order history and tracking

## 🔐 Security

- JWT token-based authentication
- Secure token storage with DataStore
- HTTPS communication
- Input validation
- ProGuard rules for release builds

## 📱 Minimum Requirements

- Android 7.0 (API 24) or higher
- 2GB RAM
- 100MB storage space
- Internet connection

## 🛠️ Build Variants

- **Debug** - Development build with logging
- **Release** - Production build with ProGuard

## 📄 License

MIT License - feel free to use this project for learning or commercial purposes.

## 👨‍💻 Author

**Rahul Mishra**
- GitHub: [@rahul700raj](https://github.com/rahul700raj)
- Email: rm2778643@gmail.com

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 TODO

- [ ] Add payment gateway integration
- [ ] Implement push notifications
- [ ] Add social login (Google, Facebook)
- [ ] Implement product reviews and ratings
- [ ] Add dark mode support
- [ ] Implement multi-language support
- [ ] Add product comparison feature
- [ ] Implement barcode scanner

## 🙏 Acknowledgments

- Material Design guidelines
- Android Jetpack libraries
- Open source community

---

Made with ❤️ for the Bloom App

## 📞 Support

For support, email rm2778643@gmail.com or create an issue in the repository.