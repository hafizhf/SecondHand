package andlima.group3.secondhand.model.lokasi


import com.google.gson.annotations.SerializedName

data class KotaResponse(
    @SerializedName("kota_kabupaten")
    val kotaKabupaten: List<KotaKabupaten>
)