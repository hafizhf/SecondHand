package andlima.group3.secondhand.model.home.newhome


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
) : Serializable