package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.model.jual.PostProductResponse
import andlima.group3.secondhand.repository.SellerRepository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SellerViewModel@Inject constructor(private val repository : SellerRepository) : ViewModel() {
    var sellerProductsLiveData : MutableLiveData<List<SellerProductsItem>> = MutableLiveData()
    var sellerPostProductLive : MutableLiveData<PostProductResponse> = MutableLiveData()
    var sellerOrdersLiveData : MutableLiveData<List<SellerOrdersItem>> = MutableLiveData()

    fun getSellerAllProductsLive(token : String){
        viewModelScope.launch {
            repository.getSellerAllProduct(token, sellerProductsLiveData)
        }
    }
    fun postProductLive(token: String, name : String, description : String, basePrice : Int, categoryIDs : List<Int>, location : String, image : MultipartBody.Part){
        viewModelScope.launch {
            val nama = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
            val deskripsi = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)
            val harga = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), basePrice.toString())
            var kategoriFiX : String = ""
            categoryIDs.forEach {
                kategoriFiX += "$it,"
            }
            val kategori = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), kategoriFiX)
            val lokasi = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location)


            repository.postProduct(token,sellerPostProductLive,nama, deskripsi, harga, kategori, lokasi, image)
        }
    }
    fun getSellerAllOrdersLive(token : String){
        viewModelScope.launch {
            repository.getSellerAllOrders(token, sellerOrdersLiveData)
        }
    }
}