package andlima.group3.secondhand.repository

import andlima.group3.secondhand.model.kategori.KategoriResponseItem
import andlima.group3.secondhand.model.register.RegisterResponse
import andlima.group3.secondhand.network.ApiService
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SellerRepository @Inject constructor(private val apiService: ApiService) {
    fun getKategoriRepo(liveData: MutableLiveData<List<KategoriResponseItem>>) {
        val call: Call<List<KategoriResponseItem>> = apiService.getCategory()
        call.enqueue(object : Callback<List<KategoriResponseItem>>{
            override fun onResponse(
                call: Call<List<KategoriResponseItem>>,
                response: Response<List<KategoriResponseItem>>
            ) {
                if (response.code() == 200){
                    liveData.postValue(response.body())
                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<List<KategoriResponseItem>>, t: Throwable) {
                liveData.postValue(null)
            }

        })
    }


}