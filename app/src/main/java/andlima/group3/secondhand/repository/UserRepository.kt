package andlima.group3.secondhand.repository

import andlima.group3.secondhand.model.register.RegisterResponse
import andlima.group3.secondhand.network.ApiService
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {
    fun registerRepo(fullName : String, email : String, password : String, phoneNumber : Int,
                     address : String,city : String,liveData: MutableLiveData<String>){
        val call : Call<RegisterResponse> = apiService.registerUser(fullName, email, password, phoneNumber, address, city)
        call?.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                liveData.postValue(response.code().toString())
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                liveData.postValue("Gagal Daftar Akun")

            }

        })

    }
}