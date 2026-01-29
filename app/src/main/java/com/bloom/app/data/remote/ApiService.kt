package com.bloom.app.data.remote

import com.bloom.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @GET("auth/me")
    suspend fun getCurrentUser(): Response<ApiResponse<User>>
    
    // Categories
    @GET("categories")
    suspend fun getCategories(): Response<CategoryResponse>
    
    @GET("categories/{id}")
    suspend fun getCategory(@Path("id") id: String): Response<ApiResponse<Category>>
    
    // Products
    @GET("products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("subcategory") subcategory: String? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 12
    ): Response<ProductResponse>
    
    @GET("products/featured")
    suspend fun getFeaturedProducts(): Response<ProductResponse>
    
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: String): Response<SingleProductResponse>
    
    // Cart
    @POST("users/cart")
    suspend fun addToCart(@Body request: Map<String, Any>): Response<ApiResponse<Any>>
    
    @PUT("users/cart/{productId}")
    suspend fun updateCartItem(
        @Path("productId") productId: String,
        @Body request: Map<String, Int>
    ): Response<ApiResponse<Any>>
    
    @DELETE("users/cart/{productId}")
    suspend fun removeFromCart(@Path("productId") productId: String): Response<ApiResponse<Any>>
    
    // Wishlist
    @POST("users/wishlist/{productId}")
    suspend fun addToWishlist(@Path("productId") productId: String): Response<ApiResponse<Any>>
    
    @DELETE("users/wishlist/{productId}")
    suspend fun removeFromWishlist(@Path("productId") productId: String): Response<ApiResponse<Any>>
    
    // Orders
    @POST("orders")
    suspend fun createOrder(@Body request: Map<String, Any>): Response<ApiResponse<Any>>
    
    @GET("orders")
    suspend fun getOrders(): Response<ApiResponse<List<Any>>>
    
    @GET("orders/{id}")
    suspend fun getOrder(@Path("id") id: String): Response<ApiResponse<Any>>
}

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)