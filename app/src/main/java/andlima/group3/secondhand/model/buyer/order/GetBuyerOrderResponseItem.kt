package andlima.group3.secondhand.model.buyer.order


import com.google.gson.annotations.SerializedName

data class GetBuyerOrderResponseItem(
    @SerializedName("base_price")
    val basePrice: String,
    @SerializedName("buyer_id")
    val buyerId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_product")
    val imageProduct: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("Product")
    val product: Product,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("transaction_date")
    val transactionDate: String,
    @SerializedName("User")
    val user: User
)