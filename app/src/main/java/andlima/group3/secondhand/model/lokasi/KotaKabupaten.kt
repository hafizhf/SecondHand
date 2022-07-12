package andlima.group3.secondhand.model.lokasi


import com.google.gson.annotations.SerializedName

data class KotaKabupaten(
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_provinsi")
    val idProvinsi: String,
    @SerializedName("nama")
    val nama: String
)