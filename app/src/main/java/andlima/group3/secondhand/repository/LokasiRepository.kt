package andlima.group3.secondhand.repository

import andlima.group3.secondhand.model.lokasi.KotaResponse
import andlima.group3.secondhand.model.lokasi.Provinsi
import andlima.group3.secondhand.model.lokasi.ProvinsiResponse
import andlima.group3.secondhand.model.user.UpdateProfileResponse
import andlima.group3.secondhand.network.ApiService
import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class LokasiRepository @Inject constructor(@Named("kota") private val apiService: ApiService) {
    fun provinsiRepo(liveData: MutableLiveData<ProvinsiResponse>){
        val call: Call<ProvinsiResponse> = apiService.getProvinsi()
        call.enqueue(object : Callback<ProvinsiResponse>{
            override fun onResponse(
                call: Call<ProvinsiResponse>,
                response: Response<ProvinsiResponse>
            ) {

                if (response.isSuccessful){
                    liveData.postValue(response.body()!!)

                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<ProvinsiResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("asdk", t.message.toString())


            }

        })
    }
    fun kotaRepo(id : Int,liveData: MutableLiveData<KotaResponse>){
        val call: Call<KotaResponse> = apiService.getKota(id)
        call.enqueue(object : Callback<KotaResponse>{
            override fun onResponse(call: Call<KotaResponse>, response: Response<KotaResponse>) {
                if (response.isSuccessful){
                    liveData.postValue(response.body()!!)

                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<KotaResponse>, t: Throwable) {
                liveData.postValue(null)
            }

        })

    }

}