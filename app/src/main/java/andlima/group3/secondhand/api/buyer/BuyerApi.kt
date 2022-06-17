package andlima.group3.secondhand.api.buyer

import andlima.group3.secondhand.model.home.BuyerProductItem
import retrofit2.Call
import retrofit2.http.GET

interface BuyerApi {
    // Request GET all product that buyer can bought
    @GET("buyer/product")
    suspend fun getAllProduct(): List<BuyerProductItem>
}