package andlima.group3.secondhand.model.produk

import andlima.group3.secondhand.model.kategori.KategoriPilihan
import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ProdukPreview(
    val name: String,

    val basePrice: String,

    val categories: @RawValue MutableSet<KategoriPilihan>,


    val description: String,



    val imageUrl: Uri?,

    val location: String,
    val sellerImage : String?,
    val sellerName : String,

    ) : Parcelable
