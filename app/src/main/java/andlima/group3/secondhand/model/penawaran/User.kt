package andlima.group3.secondhand.model.penawaran


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)