package andlima.group3.secondhand.viewmodel

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
import javax.inject.Inject

@HiltViewModel
class BuyerViewModel @Inject constructor(api: BuyerRepository): ViewModel() {

    private val apiHelper = api

    private val getAllProductData = MutableLiveData<List<BuyerProductItem>>()
    val allProductData: LiveData<List<BuyerProductItem>> = getAllProductData
    val detailProduct: MutableLiveData<BuyerProductDetail> = MutableLiveData()

    init {
        viewModelScope.launch {
            val dataProduct = api.getAllProduct()
            delay(2000)
            getAllProductData.value = dataProduct
        }
    }

    fun getDetailProduct(id: Int) {
        apiHelper.getDetailProduct(id, detailProduct)
    }
}