package andlima.group3.secondhand.model.buyer.order


import com.google.gson.annotations.SerializedName

data class DeleteOrderResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)