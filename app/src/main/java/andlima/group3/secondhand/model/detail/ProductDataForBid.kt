package andlima.group3.secondhand.model.detail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDataForBid(
    val name: String,
    val price: Int,
    val imageUrl: String
) : Parcelable

