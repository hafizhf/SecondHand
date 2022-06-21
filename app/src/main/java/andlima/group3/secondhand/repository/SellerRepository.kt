package andlima.group3.secondhand.repository

import andlima.group3.secondhand.model.daftarjual.SellerProducts
import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import andlima.group3.secondhand.model.jual.PostProductResponse
import andlima.group3.secondhand.model.kategori.KategoriResponseItem
import andlima.group3.secondhand.model.register.RegisterResponse
import andlima.group3.secondhand.model.user.UserDetailResponse
import andlima.group3.secondhand.network.ApiService
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    fun postProduct(token: String, liveData : MutableLiveData<PostProductResponse>,name : String, description : String, basePrice : Int, categoryIDs : List<Int>, location : String, image : MultipartBody.Part){
        val call : Call<PostProductResponse> = apiService.postProduct(token,name,description,basePrice,categoryIDs,location,image)
        call.enqueue(object  : Callback<PostProductResponse>{
            override fun onResponse(
                call: Call<PostProductResponse>,
                response: Response<PostProductResponse>
            ) {
                if (response.code() == 201){
                    liveData.postValue(response.body())
                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<PostProductResponse>, t: Throwable) {
                liveData.postValue(null)
            }

        })
    }
    fun getSellerAllProduct(token : String, liveData: MutableLiveData<List<SellerProductsItem>>){
        val call : Call<List<SellerProductsItem>> = apiService.getSellerAllProduct(token)
        call.enqueue(object : Callback<List<SellerProductsItem>>{
            override fun onResponse(
                call: Call<List<SellerProductsItem>>,
                response: Response<List<SellerProductsItem>>
            ) {
                if (response.code() == 200){
                    liveData.postValue(response.body())
                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<List<SellerProductsItem>>, t: Throwable) {
                liveData.postValue(null)
            }

        })

    }



}