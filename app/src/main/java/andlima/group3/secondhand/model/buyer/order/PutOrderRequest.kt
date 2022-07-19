package andlima.group3.secondhand.model.buyer.order

import com.google.gson.annotations.SerializedName

data class PutOrderRequest(
    @SerializedName("bid_price")
    val bid_price: Int
)
