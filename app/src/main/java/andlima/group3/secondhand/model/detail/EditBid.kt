package andlima.group3.secondhand.model.detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditBid(
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("base_price")
    val basePrice: Int,
    @SerializedName("bid_price")
    val bidPrice: Int,
    @SerializedName("image_url")
    val imageUrl: String
) : Parcelable
