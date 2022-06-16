package andlima.group3.secondhand.repository

import andlima.group3.secondhand.model.register.RegisterResponse
import andlima.group3.secondhand.network.ApiService
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {
    fun registerRepo(fullName : String, email : String, password : String, phoneNumber : String,
                     address : String, image : String, liveData: MutableLiveData<String>){
        val call : Call<RegisterResponse> = apiService.registerUser(fullName, email, password, phoneNumber, address, image)
        call?.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.code() == 201){
                    liveData.postValue("Berhasil Daftar Akun")
                }else{
                    liveData.postValue("Gagal Daftar Akun ${response.code()}")

                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                liveData.postValue("Gagal Daftar Akun")

            }

        })

    }
}