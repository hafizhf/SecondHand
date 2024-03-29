package andlima.group3.secondhand.model.home.newhome


import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class User(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("phone_number")
    val phoneNumber: String
) : Serializable