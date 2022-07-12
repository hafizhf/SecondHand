package andlima.group3.secondhand.model.lokasi


import com.google.gson.annotations.SerializedName

data class ProvinsiResponse(
    @SerializedName("provinsi")
    val provinsi: List<Provinsi>
)