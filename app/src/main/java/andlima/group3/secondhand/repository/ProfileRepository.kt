package andlima.group3.secondhand.repository


import andlima.group3.secondhand.model.user.UpdateProfileResponse
import andlima.group3.secondhand.network.ApiService
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiService: ApiService) {
    fun updateprofile(
        accesstoken : String, nama: RequestBody, kota: RequestBody, alamat: RequestBody, nohp: RequestBody,image : MultipartBody.Part,
        liveData: MutableLiveData<String>
    ) {
        val call: Call<UpdateProfileResponse> = apiService.profileUser(accesstoken, nama, nohp, alamat, kota, image )
        call?.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                liveData.postValue(response.code().toString())
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                liveData.postValue("Gagal Update Akun")

            }

        })

    }
}