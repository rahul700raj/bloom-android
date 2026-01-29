package com.bloom.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val _id: String,
    val name: String,
    val slug: String,
    val description: String,
    val shortDescription: String? = null,
    val price: Double,
    val comparePrice: Double? = null,
    val discount: Int = 0,
    val images: List<ProductImage>? = null,
    val category: Category? = null,
    val subcategory: Category? = null,
    val brand: String? = null,
    val sku: String? = null,
    val stock: Int = 0,
    val inStock: Boolean = true,
    val tags: List<String>? = null,
    val rating: Rating? = null,
    val isFeatured: Boolean = false,
    val isActive: Boolean = true,
    val views: Int = 0,
    val sales: Int = 0
) : Parcelable {
    fun getDiscountedPrice(): Double {
        return if (discount > 0) {
            price * (1 - discount / 100.0)
        } else {
            price
        }
    }

    fun getPrimaryImage(): String? {
        return images?.firstOrNull { it.isPrimary }?.url ?: images?.firstOrNull()?.url
    }
}

@Parcelize
data class ProductImage(
    val url: String,
    val alt: String? = null,
    val isPrimary: Boolean = false
) : Parcelable

@Parcelize
data class Rating(
    val average: Double = 0.0,
    val count: Int = 0
) : Parcelable

data class ProductResponse(
    val success: Boolean,
    val count: Int,
    val total: Int,
    val totalPages: Int,
    val currentPage: Int,
    val data: List<Product>
)

data class SingleProductResponse(
    val success: Boolean,
    val data: Product
)