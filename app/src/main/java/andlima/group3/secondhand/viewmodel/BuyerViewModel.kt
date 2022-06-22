package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.buyer.order.BuyerOrderRequest
import andlima.group3.secondhand.model.buyer.order.BuyerOrderResponse
import andlima.group3.secondhand.model.home.BuyerProductDetail
import andlima.group3.secondhand.model.home.BuyerProductItem
import andlima.group3.secondhand.repository.BuyerRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BuyerViewModel @Inject constructor(api: BuyerRepository): ViewModel() {

    private val apiHelper = api

    // GET PRODUCTS --------------------------------------------------------------------------------

    // For all products
    private val getAllProductData = MutableLiveData<List<BuyerProductItem>>()
    val allProductData: LiveData<List<BuyerProductItem>> = getAllProductData

    init {
        viewModelScope.launch {
            val dataProduct = api.getAllProduct()
//            delay(2000)
            getAllProductData.value = dataProduct
        }
    }

    /*
    * For specific category
    * id -> category_name
    * 29 -> elektronik
    * 30 -> hobi
    * 31 -> kendaraan
    * 32 -> aksesoris
    * 33 -> kesehatan
    * */
    val products: MutableLiveData<List<BuyerProductItem>> = MutableLiveData()
    fun getElektronikProducts() { apiHelper.getProductsInSpecificCategory(29, products) }
    fun getHobiProducts() { apiHelper.getProductsInSpecificCategory(30, products) }
    fun getKendaraanProducts() { apiHelper.getProductsInSpecificCategory(31, products) }
    fun getAksesorisProducts() { apiHelper.getProductsInSpecificCategory(32, products) }
    fun getKesehatanProducts() { apiHelper.getProductsInSpecificCategory(33, products) }

    // For detail
    val detailProduct: MutableLiveData<BuyerProductDetail> = MutableLiveData()
    fun getDetailProduct(id: Int) {
        apiHelper.getDetailProduct(id, detailProduct)
    }

    // POST ORDER ----------------------------------------------------------------------------------

    // Request Order
    val gotOrderResponse: MutableLiveData<Boolean> = MutableLiveData()
    fun postRequestOrder(accessToken: String, request: BuyerOrderRequest, message: (message: String) -> Unit) {
        gotOrderResponse.postValue(false)
        apiHelper.postRequestOrder(accessToken, request)
            .enqueue(object : retrofit2.Callback<BuyerOrderResponse>{
                override fun onResponse(
                    call: Call<BuyerOrderResponse>,
                    response: Response<BuyerOrderResponse>
                ) {
                    gotOrderResponse.postValue(true)
                    if (response.isSuccessful) {
                        when (response.code()) {
                            201 -> message("Tawaranmu berhasil dikirim!")
                            400 -> message("Kamu sudah mengirim tawaran untuk produk ini")
                            403 -> message(response.message())
                            500 -> message(response.message())
                            else -> message("Unknown error occurred")
                        }
                    } else {
                        message("Error occurred, try again")
                    }
                }

                override fun onFailure(call: Call<BuyerOrderResponse>, t: Throwable) {
                    gotOrderResponse.postValue(true)
                    message("Failed to connect")
                }
            })
    }
}