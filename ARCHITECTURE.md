# 🏗️ Architecture - Bloom Android App

## Overview

Bloom Android app follows **MVVM (Model-View-ViewModel)** architecture pattern with **Repository Pattern** for clean separation of concerns and maintainable code.

## Architecture Layers

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (Activities, Fragments, ViewModels)    │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Domain Layer                  │
│     (Use Cases, Business Logic)         │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│            Data Layer                   │
│  (Repositories, Data Sources, Models)   │
└─────────────────────────────────────────┘
```

## Project Structure

```
app/src/main/java/com/bloom/app/
│
├── data/                          # Data Layer
│   ├── local/                    # Local data sources
│   │   ├── PreferencesManager.kt # DataStore preferences
│   │   └── database/             # Room database
│   │       ├── BloomDatabase.kt
│   │       ├── dao/              # Data Access Objects
│   │       └── entities/         # Database entities
│   │
│   ├── remote/                   # Remote data sources
│   │   ├── ApiService.kt        # Retrofit API interface
│   │   └── ApiClient.kt         # Retrofit client setup
│   │
│   ├── model/                    # Data models
│   │   ├── User.kt
│   │   ├── Category.kt
│   │   ├── Product.kt
│   │   ├── Cart.kt
│   │   └── Order.kt
│   │
│   └── repository/               # Repository implementations
│       ├── AuthRepository.kt
│       ├── ProductRepository.kt
│       ├── CategoryRepository.kt
│       └── CartRepository.kt
│
├── ui/                           # Presentation Layer
│   ├── splash/                  # Splash screen
│   │   └── SplashActivity.kt
│   │
│   ├── auth/                    # Authentication
│   │   ├── AuthActivity.kt
│   │   └── AuthViewModel.kt
│   │
│   ├── main/                    # Main container
│   │   └── MainActivity.kt
│   │
│   ├── home/                    # Home screen
│   │   ├── HomeFragment.kt
│   │   └── HomeViewModel.kt
│   │
│   ├── categories/              # Categories
│   │   ├── CategoriesFragment.kt
│   │   └── CategoriesViewModel.kt
│   │
│   ├── product/                 # Product details
│   │   ├── ProductDetailActivity.kt
│   │   └── ProductDetailViewModel.kt
│   │
│   ├── cart/                    # Shopping cart
│   │   ├── CartActivity.kt
│   │   └── CartViewModel.kt
│   │
│   ├── checkout/                # Checkout
│   │   ├── CheckoutActivity.kt
│   │   └── CheckoutViewModel.kt
│   │
│   └── profile/                 # User profile
│       ├── ProfileFragment.kt
│       └── ProfileViewModel.kt
│
├── utils/                       # Utility classes
│   ├── Constants.kt
│   ├── Extensions.kt
│   ├── NetworkUtils.kt
│   └── ImageLoader.kt
│
└── BloomApplication.kt          # Application class
```

## MVVM Pattern

### Model
- Represents data and business logic
- Data classes for API responses
- Database entities
- Repository implementations

### View
- Activities and Fragments
- Displays data to user
- Handles user interactions
- Observes ViewModel LiveData/StateFlow

### ViewModel
- Holds UI state
- Handles business logic
- Communicates with repositories
- Survives configuration changes

## Data Flow

```
User Action → View → ViewModel → Repository → Data Source
                ↑                                    ↓
                └────────── LiveData/Flow ───────────┘
```

### Example: Loading Products

```kotlin
// 1. User opens home screen
HomeFragment.onViewCreated()

// 2. Fragment observes ViewModel
viewModel.products.observe(viewLifecycleOwner) { products ->
    adapter.submitList(products)
}

// 3. ViewModel fetches data
class HomeViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    
    fun loadProducts() {
        viewModelScope.launch {
            val result = repository.getProducts()
            _products.value = result
        }
    }
}

// 4. Repository handles data source
class ProductRepository {
    suspend fun getProducts(): List<Product> {
        return try {
            // Try remote first
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                // Cache in local database
                database.productDao().insertAll(response.body()?.data)
                response.body()?.data ?: emptyList()
            } else {
                // Fallback to local cache
                database.productDao().getAllProducts()
            }
        } catch (e: Exception) {
            // Return cached data on error
            database.productDao().getAllProducts()
        }
    }
}
```

## Key Components

### 1. ApiService (Retrofit)
```kotlin
interface ApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ProductResponse>
}
```

### 2. Repository Pattern
```kotlin
class ProductRepository(
    private val apiService: ApiService,
    private val productDao: ProductDao
) {
    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("API Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 3. ViewModel
```kotlin
class HomeViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState
    
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            repository.getProducts()
                .onSuccess { products ->
                    _uiState.value = UiState.Success(products)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message)
                }
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val products: List<Product>) : UiState()
    data class Error(val message: String?) : UiState()
}
```

### 4. Fragment/Activity
```kotlin
class HomeFragment : Fragment() {
    
    private val viewModel: HomeViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        observeUiState()
        viewModel.loadProducts()
    }
    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoading()
                    is UiState.Success -> showProducts(state.products)
                    is UiState.Error -> showError(state.message)
                }
            }
        }
    }
}
```

## Dependency Injection

Currently using manual DI. Can be upgraded to Hilt/Koin:

```kotlin
// Manual DI in Application class
class BloomApplication : Application() {
    
    lateinit var productRepository: ProductRepository
    
    override fun onCreate() {
        super.onCreate()
        
        val apiService = ApiClient.getApiService()
        val database = BloomDatabase.getInstance(this)
        
        productRepository = ProductRepository(
            apiService,
            database.productDao()
        )
    }
}
```

## State Management

### Using LiveData
```kotlin
private val _products = MutableLiveData<List<Product>>()
val products: LiveData<List<Product>> = _products
```

### Using StateFlow (Recommended)
```kotlin
private val _products = MutableStateFlow<List<Product>>(emptyList())
val products: StateFlow<List<Product>> = _products.asStateFlow()
```

## Error Handling

```kotlin
sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}

// Usage in ViewModel
fun loadProducts() {
    viewModelScope.launch {
        _products.value = Resource.Loading()
        
        try {
            val result = repository.getProducts()
            _products.value = Resource.Success(result)
        } catch (e: Exception) {
            _products.value = Resource.Error(e.message ?: "Unknown error")
        }
    }
}
```

## Navigation

Using Navigation Component:

```xml
<!-- nav_graph.xml -->
<navigation>
    <fragment
        android:id="@+id/homeFragment"
        android:name="com.bloom.app.ui.home.HomeFragment">
        <action
            android:id="@+id/action_home_to_product"
            app:destination="@id/productDetailFragment" />
    </fragment>
</navigation>
```

```kotlin
// Navigate with arguments
findNavController().navigate(
    HomeFragmentDirections.actionHomeToProduct(productId)
)
```

## Offline Support

```kotlin
class ProductRepository {
    suspend fun getProducts(): List<Product> {
        return if (NetworkUtils.isConnected()) {
            // Fetch from API and cache
            val products = apiService.getProducts().body()?.data
            database.productDao().insertAll(products)
            products ?: emptyList()
        } else {
            // Return cached data
            database.productDao().getAllProducts()
        }
    }
}
```

## Testing Strategy

### Unit Tests
- ViewModels
- Repositories
- Use Cases
- Utility functions

### Integration Tests
- Database operations
- API calls
- Repository implementations

### UI Tests
- User flows
- Screen navigation
- Form validation

## Best Practices

1. **Single Responsibility** - Each class has one job
2. **Separation of Concerns** - Clear layer boundaries
3. **Dependency Inversion** - Depend on abstractions
4. **Immutability** - Use val and data classes
5. **Coroutines** - For async operations
6. **LiveData/StateFlow** - For reactive UI
7. **ViewBinding** - Type-safe view access
8. **Repository Pattern** - Abstract data sources
9. **Error Handling** - Proper try-catch and Result types
10. **Code Documentation** - Clear comments and KDoc

## Performance Optimizations

1. **Pagination** - Load data in chunks
2. **Image Caching** - Use Glide with caching
3. **Database Indexing** - Index frequently queried fields
4. **Lazy Loading** - Load data when needed
5. **ViewHolder Pattern** - Efficient RecyclerView
6. **Coroutines** - Non-blocking operations
7. **ProGuard** - Code shrinking for release

## Future Enhancements

- [ ] Migrate to Jetpack Compose
- [ ] Add Hilt for dependency injection
- [ ] Implement Paging 3 library
- [ ] Add WorkManager for background tasks
- [ ] Implement Firebase Analytics
- [ ] Add Crashlytics for crash reporting
- [ ] Implement deep linking
- [ ] Add widget support
- [ ] Implement notification system