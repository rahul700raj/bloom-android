package com.bloom.app.data.model

data class CartItem(
    val product: Product,
    var quantity: Int = 1
) {
    fun getTotalPrice(): Double {
        return product.getDiscountedPrice() * quantity
    }
}

data class Cart(
    val items: MutableList<CartItem> = mutableListOf()
) {
    fun getTotalItems(): Int {
        return items.sumOf { it.quantity }
    }

    fun getSubtotal(): Double {
        return items.sumOf { it.getTotalPrice() }
    }

    fun getTax(): Double {
        return getSubtotal() * 0.18 // 18% GST
    }

    fun getShipping(): Double {
        return if (getSubtotal() > 500) 0.0 else 50.0
    }

    fun getTotal(): Double {
        return getSubtotal() + getTax() + getShipping()
    }

    fun addItem(product: Product, quantity: Int = 1) {
        val existingItem = items.find { it.product._id == product._id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            items.add(CartItem(product, quantity))
        }
    }

    fun removeItem(productId: String) {
        items.removeAll { it.product._id == productId }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        items.find { it.product._id == productId }?.quantity = quantity
    }

    fun clear() {
        items.clear()
    }
}