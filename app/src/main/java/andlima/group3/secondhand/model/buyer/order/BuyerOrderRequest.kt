package andlima.group3.secondhand.model.buyer.order


import com.google.gson.annotations.SerializedName

data class BuyerOrderRequest(
    @SerializedName("bid_price")
    val bidPrice: Int,
    @SerializedName("product_id")
    val productId: Int
)