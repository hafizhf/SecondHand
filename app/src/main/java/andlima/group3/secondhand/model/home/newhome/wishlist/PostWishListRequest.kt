package andlima.group3.secondhand.model.home.newhome.wishlist

import com.google.gson.annotations.SerializedName

data class PostWishListRequest(
    @SerializedName("product_id")
    val productId: Int
)
