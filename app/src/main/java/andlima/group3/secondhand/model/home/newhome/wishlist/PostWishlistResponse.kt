package andlima.group3.secondhand.model.home.newhome.wishlist


import com.google.gson.annotations.SerializedName

data class PostWishlistResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("product")
    val product: Product
)