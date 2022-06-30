package andlima.group3.secondhand.model.notification

import andlima.group3.secondhand.model.produk.ProductResponse
import com.google.gson.annotations.SerializedName

data class NotifData(
    val produk : ProductResponse,
    val respon : NotificationResponseItem

)
