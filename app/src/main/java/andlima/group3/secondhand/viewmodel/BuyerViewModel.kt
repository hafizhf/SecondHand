package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.buyer.order.*
import andlima.group3.secondhand.model.home.BuyerProductItem
import andlima.group3.secondhand.model.home.newhome.ProductDetailItemResponse
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import andlima.group3.secondhand.model.home.newhome.wishlist.DeleteWishlistResponse
import andlima.group3.secondhand.model.home.newhome.wishlist.GetWishlistResponse
import andlima.group3.secondhand.model.home.newhome.wishlist.PostWishlistResponse
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

    fun getElectronicProducts() {apiHelper.getProductsByCategory(1, electronicProducts)}
    fun getFashionProducts() {apiHelper.getProductsByCategory(4, fashionProducts)}
    fun getFoodProducts() {apiHelper.getProductsByCategory(10, foodProducts)}
    fun getHomeSuppliesProducts() {apiHelper.getProductsByCategory(12, homeSuppliesProducts)}

    // For HomeElectronicFragment --------------
    val phoneProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    val computerProducts: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()

    fun getPhoneProducts() {apiHelper.getProductsByCategory(3, phoneProducts)}
    fun getComputerProducts() {apiHelper.getProductsByCategory(2, computerProducts)}

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

    fun getManClothesProducts() {apiHelper.getProductsByCategory(4, manClothesProducts)}
    fun getManShoesProducts() {apiHelper.getProductsByCategory(5, manShoesProducts)}
    fun getManBagProducts() {apiHelper.getProductsByCategory(6, manBagProducts)}
    fun getWomanClothesProducts() {apiHelper.getProductsByCategory(13, womanClothesProducts)}
    fun getWomanShoesProducts() {apiHelper.getProductsByCategory(17, womanShoesProducts)}
    fun getWomanBagProducts() {apiHelper.getProductsByCategory(18, womanBagProducts)}
    fun getMuslimFashionProducts() {apiHelper.getProductsByCategory(14, muslimFashionProducts)}
    fun getKidFashionProducts() {apiHelper.getProductsByCategory(15, kidFashionProducts)}
    fun getAccessoriesFashionProducts() {apiHelper.getProductsByCategory(7, accessoriesFashionProducts)}

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

    fun getBooksProducts() {apiHelper.getProductsByCategory(21, booksProducts)}
    fun getSouvenirProducts() {apiHelper.getProductsByCategory(23, souvenirProducts)}
    fun getPhotographyProducts() {apiHelper.getProductsByCategory(24, photographyProducts)}
    fun getHealthProducts() {apiHelper.getProductsByCategory(8, healthProducts)}
    fun getHobbyProducts() {apiHelper.getProductsByCategory(9, hobbyProducts)}
    fun getBeautyProducts() {apiHelper.getProductsByCategory(11, beautyProducts)}
    fun getMotherChildProducts() {apiHelper.getProductsByCategory(16, motherChildProducts)}
    fun getAutomotiveProducts() {apiHelper.getProductsByCategory(19, automotiveProducts)}
    fun getSportsProducts() {apiHelper.getProductsByCategory(20, sportsProducts)}
    fun getVoucherProducts() {apiHelper.getProductsByCategory(22, voucherProducts)}

    // Buyer Order
    val orderQuantity: MutableLiveData<Int> = MutableLiveData()
    fun getBuyerOrderQuantity(accessToken: String) {
        apiHelper.checkBuyerOrderQuantity(accessToken, orderQuantity)
    }

    val orderList: MutableLiveData<List<GetBuyerOrderResponseItem>> = MutableLiveData()
    fun getBuyerOrderList(accessToken: String) {
        apiHelper.getBuyerOrderData(accessToken, orderList)
    }

    // Get search result
    val searchResult: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()
    fun getSearchResult(keyword: String) {
        apiHelper.getSearchResult(keyword, searchResult)
    }

    // Delete order
    val deleteResponse: MutableLiveData<DeleteOrderResponse> = MutableLiveData()
    fun deleteOrder(accessToken: String, id: Int) {
        apiHelper.deleteOrder(accessToken, id, deleteResponse)
    }

    // Edit order
    val editResponse: MutableLiveData<PutOrderResponse> = MutableLiveData()
    fun editOrder(accessToken: String, orderId: Int, newBidPrice: Int) {
        apiHelper.editOrder(accessToken, orderId, newBidPrice, editResponse)
    }

    // =============================================================================================


//    private val cobacoba: MutableLiveData<List<ProductItemResponse>> = MutableLiveData()


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

    // BUYER WISHLIST ------------------------------------------------------------------------------

    val wishlistList: MutableLiveData<List<GetWishlistResponse>> = MutableLiveData()
    fun getWishlist(accessToken: String) {apiHelper.getWishlist(accessToken, wishlistList)}

    val wishlistDelete: MutableLiveData<DeleteWishlistResponse> = MutableLiveData()
    fun deleteWishlist(accessToken: String, id: Int) {apiHelper.deleteWishlist(accessToken, id, wishlistDelete)}

    val wishlistPost: MutableLiveData<PostWishlistResponse> = MutableLiveData()
    fun postWishlist(accessToken: String, id: Int) {apiHelper.postWishlist(accessToken, id, wishlistPost)}

    val wishlistQuantity: MutableLiveData<Int> = MutableLiveData()
    fun getWishlistQuantity(accessToken: String) {
        apiHelper.checkBuyerWishlistQuantity(accessToken, wishlistQuantity)
    }

//    private val isProductWishListed: MutableLiveData<GetWishlistResponse> = MutableLiveData()

    // BANNER --------------------------------------------------------------------------------------

//    val bannerList: MutableLiveData<List<GetBannerResponse>> = MutableLiveData()
//    fun getBanner() {apiHelper.getBanner(bannerList)}
}