package andlima.group3.secondhand.model.detail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditBid(
    val orderId: Int,
    val name: String,
    val basePrice: Int,
    val bidPrice: Int,
    val imageUrl: String
) : Parcelable
