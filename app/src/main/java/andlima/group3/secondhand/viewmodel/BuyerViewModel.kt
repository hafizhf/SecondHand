package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.buyer.order.BuyerOrderRequest
import andlima.group3.secondhand.model.buyer.order.BuyerOrderResponse
import andlima.group3.secondhand.model.buyer.order.GetBuyerOrderResponseItem
import andlima.group3.secondhand.model.home.BuyerProductDetail
import andlima.group3.secondhand.model.home.BuyerProductItem
import andlima.group3.secondhand.model.home.newhome.ProductDetailItemResponse
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import andlima.group3.secondhand.repository.BuyerRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // INI YG BARU =================================================================================
    // For HomeFragment ------------------

    val allProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    fun getAllProducts() {apiHelper.getAllNewProduct(allProducts)}

    val electronicProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val fashionProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val foodProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val homeSuppliesProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()

    fun getElectronicProducts() {apiHelper.getProductsByCategory(96, electronicProducts)}
    fun getFashionProducts() {apiHelper.getProductsByCategory(99, fashionProducts)}
    fun getFoodProducts() {apiHelper.getProductsByCategory(105, foodProducts)}
    fun getHomeSuppliesProducts() {apiHelper.getProductsByCategory(107, homeSuppliesProducts)}

    // For HomeElectronicFragment --------------
    val phoneProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val computerProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()

    fun getPhoneProducts() {apiHelper.getProductsByCategory(98, phoneProducts)}
    fun getComputerProducts() {apiHelper.getProductsByCategory(97, computerProducts)}

    // For HomeFashionFragment
    val manClothesProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val manShoesProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val manBagProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val womanClothesProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val womanShoesProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val womanBagProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val muslimFashionProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val kidFashionProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val accessoriesFashionProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()

    fun getManClothesProducts() {apiHelper.getProductsByCategory(99, manClothesProducts)}
    fun getManShoesProducts() {apiHelper.getProductsByCategory(100, manShoesProducts)}
    fun getManBagProducts() {apiHelper.getProductsByCategory(101, manBagProducts)}
    fun getWomanClothesProducts() {apiHelper.getProductsByCategory(108, womanClothesProducts)}
    fun getWomanShoesProducts() {apiHelper.getProductsByCategory(112, womanShoesProducts)}
    fun getWomanBagProducts() {apiHelper.getProductsByCategory(113, womanBagProducts)}
    fun getMuslimFashionProducts() {apiHelper.getProductsByCategory(109, muslimFashionProducts)}
    fun getKidFashionProducts() {apiHelper.getProductsByCategory(110, kidFashionProducts)}
    fun getAccessoriesFashionProducts() {apiHelper.getProductsByCategory(110, accessoriesFashionProducts)}

    // HomeMoreCategory
    val booksProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val souvenirProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val photographyProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val healthProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val hobbyProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val beautyProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val motherChildProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val automotiveProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val sportsProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val voucherProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()

    fun getBooksProducts() {apiHelper.getProductsByCategory(116, booksProducts)}
    fun getSouvenirProducts() {apiHelper.getProductsByCategory(118, souvenirProducts)}
    fun getPhotographyProducts() {apiHelper.getProductsByCategory(119, photographyProducts)}
    fun getHealthProducts() {apiHelper.getProductsByCategory(103, healthProducts)}
    fun getHobbyProducts() {apiHelper.getProductsByCategory(104, hobbyProducts)}
    fun getBeautyProducts() {apiHelper.getProductsByCategory(106, beautyProducts)}
    fun getMotherChildProducts() {apiHelper.getProductsByCategory(111, motherChildProducts)}
    fun getAutomotiveProducts() {apiHelper.getProductsByCategory(114, automotiveProducts)}
    fun getSportsProducts() {apiHelper.getProductsByCategory(115, sportsProducts)}
    fun getVoucherProducts() {apiHelper.getProductsByCategory(117, sportsProducts)}

    // Buyer Order
    val orderQuantity: MutableLiveData<Int> = MutableLiveData()
    fun getBuyerOrderQuantity(accessToken: String) {
        apiHelper.checkBuyerOrderQuantity(accessToken, orderQuantity)
    }

    val orderList: MutableLiveData<List<GetBuyerOrderResponseItem>> = MutableLiveData()
    fun getBuyerOrderList(accessToken: String) {
        apiHelper.getBuyerOrderData(accessToken, orderList)
    }

    // =============================================================================================



    fun getProductsByCategory(id: Int) {apiHelper.getProductsByCategory(id, cobacoba)}
    val cobacoba: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()


    // INI YG LAMA -----------------------------
    fun getElektronikProducts() { apiHelper.getProductsInSpecificCategory(29, products) }
    fun getHobiProducts() { apiHelper.getProductsInSpecificCategory(30, products) }
    fun getKendaraanProducts() { apiHelper.getProductsInSpecificCategory(31, products) }
    fun getAksesorisProducts() { apiHelper.getProductsInSpecificCategory(32, products) }
    fun getKesehatanProducts() { apiHelper.getProductsInSpecificCategory(33, products) }

    // For detail
    val detailProduct: MutableLiveData<ProductDetailItemResponse> = MutableLiveData()
    fun getDetailProduct(id: Int) {
        apiHelper.getDetailProduct(id, detailProduct)
    }

    // Check if product owned by seller
    val isSellerProduct: MutableLiveData<Boolean> = MutableLiveData()
    fun checkProductOwnedBySeller(accessToken: String, id: Int) {
        apiHelper.checkProductOwnedBySeller(accessToken, id, isSellerProduct)
    }

    // POST ORDER ----------------------------------------------------------------------------------

    // Request Order
    val gotOrderResponse: MutableLiveData<Boolean> = MutableLiveData()
    fun postRequestOrder(accessToken: String, request: BuyerOrderRequest, message: (code: Int, message: String) -> Unit) {
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
                            201 -> message(201, "Tawaranmu berhasil dikirim!")
                            400 -> message(400, "Kamu sudah mengirim tawaran untuk produk ini")
                            403 -> message(403, response.message())
                            500 -> message(500, response.message())
                            else -> message(-1, "Unknown error occurred")
                        }
                    } else {
                        message(-100, "Error occurred, try again")
                    }
                }

                override fun onFailure(call: Call<BuyerOrderResponse>, t: Throwable) {
                    gotOrderResponse.postValue(true)
                    message(-2, "Failed to connect")
                }
            })
    }
}