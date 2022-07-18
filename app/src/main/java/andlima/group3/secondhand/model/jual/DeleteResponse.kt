package andlima.group3.secondhand.model.jual


import com.google.gson.annotations.SerializedName

data class DeleteResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("name")
    val name: String
)