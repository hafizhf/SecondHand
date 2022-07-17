package andlima.group3.secondhand.api.buyer

import andlima.group3.secondhand.model.buyer.order.*
import andlima.group3.secondhand.model.home.BuyerProductDetail
import andlima.group3.secondhand.model.home.BuyerProductItem
import andlima.group3.secondhand.model.home.newhome.ProductDetailItemResponse
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import andlima.group3.secondhand.model.home.newhome.wishlist.DeleteWishlistResponse
import andlima.group3.secondhand.model.home.newhome.wishlist.GetWishlistResponse
import andlima.group3.secondhand.model.home.newhome.wishlist.PostWishListRequest
import andlima.group3.secondhand.model.home.newhome.wishlist.PostWishlistResponse
import retrofit2.Call
import retrofit2.http.*

interface BuyerApi {

    // BUYER PRODUCT -------------------------------------------------------------------------------

    // Request all product that buyer can bought
    @GET("buyer/product")
    suspend fun getAllProduct(): List<BuyerProductItem>

    // Request all product that buyer can bought
    @GET("buyer/product")
    fun getAllNewProduct(): Call<List<ProductItemResponse>>

    // Request detail product data
    @GET("buyer/product")
    fun getProductsInSpecificCategory(
        @Query("category_id") category_id: Int
    ): Call<List<BuyerProductItem>>

    // Request detail product data
    @GET("buyer/product")
    fun getProductsByCategory(
        @Query("category_id") category_id: Int
    ): Call<List<ProductItemResponse>>

    // Request detail product data
    @GET("buyer/product")
    fun getSearchResult(
        @Query("search") search: String
    ): Call<List<ProductItemResponse>>

    // Request detail product data
    @GET("buyer/product/{id}")
    fun getDetailProduct(@Path("id") id: Int): Call<ProductDetailItemResponse>


    // Request detail product data
    @GET("seller/product/{id}")
    fun getSellerDetailProduct(
        @Header("access_token") accessToken: String,
        @Path("id") id: Int
    ): Call<BuyerProductDetail>

    // BUYER ORDER -------------------------------------------------------------------------------

    // Request order
    @POST("buyer/order")
    fun postRequestOrder(
        @Header("access_token") accessToken: String,
        @Body request: BuyerOrderRequest
    ) : Call<BuyerOrderResponse>

    // Get buyer order
    @GET("buyer/order")
    fun getOrderList(
        @Header("access_token") accessToken: String
    ) : Call<List<GetBuyerOrderResponseItem>>

    // Edit order by id
    @PUT("buyer/order/{id}")
    fun editOrderBid(
        @Header("access_token") accessToken: String,
        @Path("id") id: Int,
        @Field("bid_price") bidPrice: Int
    ) : Call<PutOrderResponse>

    // Delete order by id
    @DELETE("buyer/order/{id}")
    fun deleteOrder(
        @Header("access_token") accessToken: String,
        @Path("id") id: Int
    ) : Call<DeleteOrderResponse>

    // BUYER WISHLIST ------------------------------------------------------------------------------

    @POST("buyer/wishlist")
    fun postUserWishlist(
        @Header("access_token") accessToken: String,
        @Body request: PostWishListRequest
    ): Call<PostWishlistResponse>

    @GET("buyer/wishlist")
    fun getUserWishlist(
        @Header("access_token") accessToken: String
    ): Call<List<GetWishlistResponse>>

    @DELETE("buyer/wishlist/{id}")
    fun deleteUserWishlist(
        @Header("access_token") accessToken: String,
        @Path("id") id: Int
    ): Call<DeleteWishlistResponse>
}