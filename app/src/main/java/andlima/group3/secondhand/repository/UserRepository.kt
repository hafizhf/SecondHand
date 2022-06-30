package andlima.group3.secondhand.repository

import andlima.group3.secondhand.SingleLiveEvent.SingleLiveMutableData
import andlima.group3.secondhand.model.notification.NotifData
import andlima.group3.secondhand.model.notification.NotificationResponseItem
import andlima.group3.secondhand.model.produk.ProductResponse
import andlima.group3.secondhand.model.register.RegisterResponse
import andlima.group3.secondhand.model.user.UserDetailResponse
import andlima.group3.secondhand.network.ApiService
import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Handler
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {
    val listProduk : MutableList<ProductResponse> = mutableListOf()

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
    fun getDetailUser(token : String, liveData : MutableLiveData<UserDetailResponse>){
        val call : Call<UserDetailResponse> = apiService.getDetailUser(token)
        call.enqueue(object  : Callback<UserDetailResponse>{
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {if (response.isSuccessful){
                liveData.postValue(response.body()!!)

            }else{
                liveData.postValue(null)

            }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                liveData.postValue(null)
            }

        })
    }
    fun getNotifRepo(token: String, liveTrue : MutableList<NotificationResponseItem>?, liveFalse : MutableList<NotificationResponseItem>?){
        val call : Call<List<NotificationResponseItem>> = apiService.getNotif(token)
        call.enqueue(object : Callback<List<NotificationResponseItem>>{
            override fun onResponse(
                call: Call<List<NotificationResponseItem>>,
                response: Response<List<NotificationResponseItem>>
            ) {
                if (response.isSuccessful){


                    response.body()!!.forEach {
                        if (it.read){
                            liveTrue!!.add(it)
                        }else{
                            liveFalse!!.add(it)
                        }
                    }





                }else{


                }
            }

            override fun onFailure(call: Call<List<NotificationResponseItem>>, t: Throwable) {

            }

        })
    }
    fun tambah(response: ProductResponse){
        listProduk.add(response)
    }
    fun getDetailProduk(token : String,listId : MutableList<Int>, liveData: SingleLiveMutableData<List<ProductResponse>> )  {
        listId.forEach {
            android.os.Handler().postDelayed({
                val call: Call<ProductResponse> = apiService.getDetailProduct(token, it)
                call.enqueue(object : Callback<ProductResponse>{
                    override fun onResponse(
                        call: Call<ProductResponse>,
                        response: Response<ProductResponse>
                    ) {
                        if(response.isSuccessful){
                            tambah(response.body()!!)

                        }

                    }

                    override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }, 200)

            android.os.Handler().postDelayed({
                liveData.postValue(listProduk)
                Log.d("LISTLIST", listProduk.toString())

                Log.d("losss", liveData.value.toString())
            },3000)




        }
    }
}