package com.zano.mistcafe.api

import com.zano.mistcafe.db.ApiResponse
import com.zano.mistcafe.db.AuthResponse
import com.zano.mistcafe.db.CartItem
import com.zano.mistcafe.db.FavoriteResponse
import com.zano.mistcafe.db.OrderHistoryItem
import com.zano.mistcafe.db.Product
import com.zano.mistcafe.db.UserProfile
import com.zano.mistcafe.network.ProductDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface CoffeeApiService {


    @GET("get_products.php")
    suspend fun getProductsByCategory(
        @Query("category") category: String,
    ): List<Product>


    @GET("get_cart.php")
    suspend fun getCart(
        @Query("user_id") userId: Int,
    ): List<CartItem>

    @FormUrlEncoded
    @POST("remove_cart_item.php")
    suspend fun removeCartItem(
        @Field("cart_id") cartId: Int,
    ): ApiResponse


    @FormUrlEncoded
    @POST("add_to_cart.php")
    suspend fun addToCart(
        @Field("user_id") userId: Int,
        @Field("product_id") productId: Int,
        @Field("product_name") productName: String,
        @Field("image_url") imageUrl: String,
        @Field("price") price: Double,
    ): ApiResponse


    @GET("get_fav_ids.php")
    suspend fun getFavoriteIds(
        @Query("user_id") userId: Int,
    ): List<Int>

    @GET("get_fav_products.php")
    suspend fun getFavoriteProducts(
        @Query("user_id") userId: Int,
    ): List<ProductDto>


    @FormUrlEncoded
    @POST("toggle_fav.php")
    suspend fun toggleFavorite(
        @Field("user_id") userId: Int,
        @Field("product_id") productId: Int,
    ): FavoriteResponse


    @GET("get_profile.php")
    suspend fun getProfile(
        @Query("user_id") userId: Int,
    ): UserProfile

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse


    @FormUrlEncoded
    @POST("save_orders.php")
    suspend fun saveCompletedProducts(
        @Field("user_id") userId: Int,
        @Field("products") productsJson: String
    ): ApiResponse

    @GET("get_orders.php")
    suspend fun getUserOrders(
        @Query("user_id") userId: Int
    ): List<OrderHistoryItem>
}
