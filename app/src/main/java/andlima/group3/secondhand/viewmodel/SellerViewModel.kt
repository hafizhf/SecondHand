package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.model.daftarjual.terimatolak.PatchOrderResponse
import andlima.group3.secondhand.model.jual.DeleteResponse
import andlima.group3.secondhand.model.jual.EditResponse
import andlima.group3.secondhand.model.jual.PatchResponse
import andlima.group3.secondhand.model.jual.PostProductResponse
import andlima.group3.secondhand.repository.SellerRepository
import android.util.Log

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
    var sellerSoldLiveData : MutableLiveData<List<SellerOrdersItem>> = MutableLiveData()

    var sellerPostProductLive : MutableLiveData<PostProductResponse> = MutableLiveData()
    var sellerEditProductLive : MutableLiveData<EditResponse> = MutableLiveData()
    var sellerDeleteProductLive : MutableLiveData<DeleteResponse> = MutableLiveData()
    var sellerOrdersLiveData : MutableLiveData<List<SellerOrdersItem>> = MutableLiveData()
    var sellerBuyerOrdersLiveData : MutableLiveData<List<SellerOrdersItem>> = MutableLiveData()
    var patchOrderLiveData : MutableLiveData<PatchOrderResponse> = MutableLiveData()
    var patchProductLiveData : MutableLiveData<PatchResponse> = MutableLiveData()

    var sellerDetailOrdersLiveData : MutableLiveData<SellerOrdersItem> = MutableLiveData()

    fun getSellerAllProductsLive(token : String){
        viewModelScope.launch {
            repository.getSellerAllProduct(token, sellerProductsLiveData)
        }
    }
    fun getSellerSoldProductsLive(token : String){
        viewModelScope.launch {
            repository.getSellerSoldProduct(token, sellerSoldLiveData)
        }
    }
    fun patchOrderLive(token: String, id: Int, status : String){
        viewModelScope.launch {
            repository.patchOrderRepo(token, id, status, patchOrderLiveData)
        }
    }
    fun patchProductLive(token: String, id: Int, status : String){
        viewModelScope.launch {
            repository.patchSellerProduct(token, id, status, patchProductLiveData)
        }
    }

    fun postProductLive(token: String, name : String, description : String, basePrice : Int, categoryIDs : List<Int>, location : String, image : MultipartBody.Part){
        viewModelScope.launch {
            val nama = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
            val deskripsi = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)
            val harga = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), basePrice.toString())
            var kategoriFiX : String = categoryIDs.toString().replace("[", "").replace("]","")

            val kategori = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), kategoriFiX)
            val lokasi = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location)


            repository.postProduct(token,sellerPostProductLive,nama, deskripsi, harga, kategori, lokasi, image)
        }
    }
    fun editProductLive(token: String, id: Int,name : String, description : String, basePrice : Int, categoryIDs : List<Int>, location : String, image : MultipartBody.Part?){
        viewModelScope.launch {
            val nama = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
            val deskripsi = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)
            val harga = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), basePrice.toString())
            var kategoriFiX : String = categoryIDs.toString().replace("[", "").replace("]","")
            Log.d("kategoriku", kategoriFiX)
            val kategori = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), kategoriFiX)
            val lokasi = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location)


            repository.editProduct(token,id,sellerEditProductLive,nama, deskripsi, harga, kategori, lokasi, image)
        }
    }
    fun deleteProductLive(token: String, id: Int){
        viewModelScope.launch {
            repository.deleteProduct(token,id, sellerDeleteProductLive)
        }
    }

    fun getSellerAllOrdersLive(token : String){
        viewModelScope.launch {
            repository.getSellerAllOrders(token, sellerOrdersLiveData)
        }
    }
    fun getBuyerOrdersLive(token: String, id: Int){
        viewModelScope.launch {
            repository.getSellerAllBuyerOrders(token,id,sellerBuyerOrdersLiveData)
        }
    }
    fun getDetailOrderLive(token: String, id : Int){
        viewModelScope.launch {
            repository.getDetailOrder(token,id,sellerDetailOrdersLiveData)
        }
    }
}