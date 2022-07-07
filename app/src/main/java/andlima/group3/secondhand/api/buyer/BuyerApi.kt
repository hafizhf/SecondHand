package andlima.group3.secondhand.api.buyer

import andlima.group3.secondhand.model.buyer.order.BuyerOrderRequest
import andlima.group3.secondhand.model.buyer.order.BuyerOrderResponse
import andlima.group3.secondhand.model.home.BuyerProductDetail
import andlima.group3.secondhand.model.home.BuyerProductItem
import andlima.group3.secondhand.model.home.newhome.ProductDetailItemResponse
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import retrofit2.Call
import retrofit2.http.*

interface BuyerApi {
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
    @GET("buyer/product/{id}")
    fun getDetailProduct(@Path("id") id: Int): Call<ProductDetailItemResponse>

    // Request order
    @POST("buyer/order")
    fun postRequestOrder(
        @Header("access_token") accessToken: String,
        @Body request: BuyerOrderRequest
    ) : Call<BuyerOrderResponse>

    // Request detail product data
    @GET("seller/product/{id}")
    fun getSellerDetailProduct(
        @Header("access_token") accessToken: String,
        @Path("id") id: Int
    ): Call<BuyerProductDetail>
}