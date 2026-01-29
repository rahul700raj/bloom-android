package com.bloom.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val _id: String,
    val name: String,
    val slug: String,
    val description: String? = null,
    val image: String? = null,
    val icon: String? = null,
    val parentCategory: String? = null,
    val subcategories: List<Category>? = null,
    val isActive: Boolean = true,
    val order: Int = 0
) : Parcelable

data class CategoryResponse(
    val success: Boolean,
    val count: Int,
    val data: List<Category>
)