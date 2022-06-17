package andlima.group3.secondhand.model.kategori


import com.google.gson.annotations.SerializedName

data class KategoriResponseItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)