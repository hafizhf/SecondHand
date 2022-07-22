package andlima.group3.secondhand.repository

import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.model.daftarjual.terimatolak.PatchOrderResponse
import andlima.group3.secondhand.model.daftarjual.terimatolak.StatusTawaran
import andlima.group3.secondhand.model.jual.DeleteResponse
import andlima.group3.secondhand.model.jual.EditResponse
import andlima.group3.secondhand.model.jual.PatchResponse
import andlima.group3.secondhand.model.jual.PostProductResponse
import andlima.group3.secondhand.model.kategori.KategoriResponseItem
import andlima.group3.secondhand.network.ApiService
import android.util.Log
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

    fun postProduct(token: String, liveData : MutableLiveData<PostProductResponse>,name : RequestBody, description : RequestBody, basePrice : RequestBody, categoryIDs : RequestBody, location : RequestBody, image : MultipartBody.Part){
        val call : Call<PostProductResponse> = apiService.postProduct(token,image,name,description,basePrice,categoryIDs,location)
        call.enqueue(object  : Callback<PostProductResponse>{
            override fun onResponse(
                call: Call<PostProductResponse>,
                response: Response<PostProductResponse>
            ) {
                if (response.code() == 201){
                    liveData.postValue(response.body())
                    Log.d("PESAN", response.message())

                }else{
                    liveData.postValue(null)
                    Log.d("PESAN", response.message())


                }
            }

            override fun onFailure(call: Call<PostProductResponse>, t: Throwable) {
                Log.d("PESAN", t.message.toString())

                liveData.postValue(null)
            }

        })
    }
    fun editProduct(token: String, id : Int,liveData : MutableLiveData<EditResponse>,name : RequestBody, description : RequestBody, basePrice : RequestBody, categoryIDs : RequestBody, location : RequestBody, image : MultipartBody.Part?){
        val call : Call<EditResponse> = apiService.editProduct(token, id,image,name,description,basePrice,categoryIDs,location)
        call.enqueue(object  : Callback<EditResponse>{
            override fun onResponse(
                call: Call<EditResponse>,
                response: Response<EditResponse>
            ) {
                liveData.postValue(response.body())


            }

            override fun onFailure(call: Call<EditResponse>, t: Throwable) {
                Log.d("PESAN", t.message.toString())

                liveData.postValue(null)
            }

        })
    }
    fun deleteProduct(token : String, id : Int,liveData: MutableLiveData<DeleteResponse>){
        val call : Call<DeleteResponse> = apiService.deleteProduct(token,id)
        call.enqueue(object : Callback<DeleteResponse>{
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                liveData.postValue(response.body())

            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
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
    fun getSellerSoldProduct(token : String, liveData: MutableLiveData<List<SellerOrdersItem>>){
        val call : Call<List<SellerOrdersItem>> = apiService.getSellerSoldOrder(token)
        call.enqueue(object : Callback<List<SellerOrdersItem>>{
            override fun onResponse(
                call: Call<List<SellerOrdersItem>>,
                response: Response<List<SellerOrdersItem>>
            ) {
                if (response.code() == 200){

                    liveData.postValue(response.body())
                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<List<SellerOrdersItem>>, t: Throwable) {
                liveData.postValue(null)
            }

        })

    }


    fun getSellerAllOrders(token: String, liveData: MutableLiveData<List<SellerOrdersItem>>){
        val call : Call<List<SellerOrdersItem>> = apiService.getSellerAllOrder(token)
        call.enqueue(object  : Callback<List<SellerOrdersItem>>{
            override fun onResponse(
                call: Call<List<SellerOrdersItem>>,
                response: Response<List<SellerOrdersItem>>
            ) {
                liveData.postValue(response.body())

                if (response.code() == 200){
                    liveData.postValue(response.body())
                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<List<SellerOrdersItem>>, t: Throwable) {
                liveData.postValue(null)
            }

        })

    }
    fun getSellerAllBuyerOrders(token: String, id: Int,liveData: MutableLiveData<List<SellerOrdersItem>>){
        val call : Call<List<SellerOrdersItem>> = apiService.getSellerAllOrder(token)
        call.enqueue(object  : Callback<List<SellerOrdersItem>>{
            override fun onResponse(
                call: Call<List<SellerOrdersItem>>,
                response: Response<List<SellerOrdersItem>>
            ) {
                if (response.code() == 200){
                    val listOrder : MutableList<SellerOrdersItem> = mutableListOf()
                    response.body()!!.forEach {
                        if (it.buyerId == id){
                            listOrder.add(it)
                        }
                    }
                    liveData.postValue(listOrder)
                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<List<SellerOrdersItem>>, t: Throwable) {
                liveData.postValue(null)
            }

        })

    }
    fun patchSellerProduct(token: String, id: Int, status: String, liveData: MutableLiveData<PatchResponse>){
        val call : Call<PatchResponse> = apiService.patchProductSeller(token, id, status)
        call.enqueue(object :Callback<PatchResponse>{
            override fun onResponse(call: Call<PatchResponse>, response: Response<PatchResponse>) {
                liveData.postValue(response.body())
            }

            override fun onFailure(call: Call<PatchResponse>, t: Throwable) {
                liveData.postValue(null)
            }

        })

    }

    fun getDetailOrder(token: String, id : Int, liveData : MutableLiveData<SellerOrdersItem>){
        val call : Call<SellerOrdersItem> = apiService.getDetailOrder(token, id)
        call.enqueue(object  : Callback<SellerOrdersItem>{
            override fun onResponse(
                call: Call<SellerOrdersItem>,
                response: Response<SellerOrdersItem>
            ) {
                if (response.isSuccessful){
                    liveData.postValue(response.body()!!)
                }else{
                    liveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<SellerOrdersItem>, t: Throwable) {
                liveData.postValue(null)

            }

        })

    }

    fun patchOrderRepo(token: String, id: Int, status : String, liveData : MutableLiveData<PatchOrderResponse>){
        val call : Call<PatchOrderResponse> = apiService.prosesOrder(token, id, StatusTawaran(status))
        call.enqueue(object  : Callback<PatchOrderResponse>{
            override fun onResponse(
                call: Call<PatchOrderResponse>,
                response: Response<PatchOrderResponse>
            ) {
                if (response.isSuccessful){
                    liveData.postValue(response.body()!!)
                }else{
                    liveData.postValue(null)

                }
            }

            override fun onFailure(call: Call<PatchOrderResponse>, t: Throwable) {
                liveData.postValue(null)

            }

        })

    }



}