package andlima.group3.secondhand.model.penawaran


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellerOrdersItem(
    @SerializedName("buyer_id")
    val buyerId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("Product")
    val product: Product,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("User")
    val user: User
) : Parcelable