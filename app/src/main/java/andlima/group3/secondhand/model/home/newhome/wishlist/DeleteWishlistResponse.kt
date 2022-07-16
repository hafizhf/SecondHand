package andlima.group3.secondhand.model.home.newhome.wishlist


import com.google.gson.annotations.SerializedName

data class DeleteWishlistResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)