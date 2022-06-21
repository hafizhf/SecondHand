package andlima.group3.secondhand.api.buyer

import andlima.group3.secondhand.model.home.BuyerProductDetail
import andlima.group3.secondhand.model.home.BuyerProductItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BuyerApi {
    // Request all product that buyer can bought
    @GET("buyer/product")
    suspend fun getAllProduct(): List<BuyerProductItem>

    // Request detail product data
    @GET("buyer/product/{id}")
    fun getDetailProduct(@Path("id") id: Int): Call<BuyerProductDetail>
}