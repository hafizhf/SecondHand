package andlima.group3.secondhand.repository

import andlima.group3.secondhand.api.buyer.BuyerApi
import andlima.group3.secondhand.model.buyer.order.BuyerOrderRequest
import andlima.group3.secondhand.model.home.BuyerProductDetail
import andlima.group3.secondhand.model.home.BuyerProductItem
import andlima.group3.secondhand.model.home.newhome.ProductDetailItemResponse
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class BuyerRepository @Inject constructor(private val api: BuyerApi) {

    // GET PRODUCTS --------------------------------------------------------------------------------

    // To get all products
    suspend fun getAllProduct() = api.getAllProduct()

    // To get all products
    fun getAllNewProduct(data: MutableLiveData<List<ProductItemResponse>>) {
        api.getAllNewProduct().enqueue(object : retrofit2.Callback<List<ProductItemResponse>>{
            override fun onResponse(
                call: Call<List<ProductItemResponse>>,
                response: Response<List<ProductItemResponse>>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        data.postValue(response.body())
                    } else {
                        Log.d("ERROR CODE", response.code().toString())
                        data.postValue(null)
                    }
                } else {
                    data.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<ProductItemResponse>>, t: Throwable) {
                data.postValue(null)
            }

        })
    }

    // TO get products in a specific category
    fun getProductsInSpecificCategory(id: Int, data: MutableLiveData<List<BuyerProductItem>>) {
        api.getProductsInSpecificCategory(id)
            .enqueue(object : retrofit2.Callback<List<BuyerProductItem>>{
                override fun onResponse(
                    call: Call<List<BuyerProductItem>>,
                    response: Response<List<BuyerProductItem>>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            data.postValue(response.body())
                        } else {
                            Log.d("ERROR CODE", response.code().toString())
                            data.postValue(null)
                        }
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(call: Call<List<BuyerProductItem>>, t: Throwable) {
                    data.postValue(null)
                }
            })
    }

    // TO get products in a specific category
    fun getProductsByCategory(id: Int, data: MutableLiveData<List<ProductItemResponse>>) {
        api.getProductsByCategory(id)
            .enqueue(object : retrofit2.Callback<List<ProductItemResponse>>{
                override fun onResponse(
                    call: Call<List<ProductItemResponse>>,
                    response: Response<List<ProductItemResponse>>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            data.postValue(response.body())
                        } else {
                            Log.d("ERROR CODE", response.code().toString())
                            data.postValue(null)
                        }
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(call: Call<List<ProductItemResponse>>, t: Throwable) {
                    data.postValue(null)
                }
            })
    }

    // To get product detail
    fun getDetailProduct(id: Int, data: MutableLiveData<ProductDetailItemResponse>) {
        api.getDetailProduct(id).enqueue(object : retrofit2.Callback<ProductDetailItemResponse>{
            override fun onResponse(
                call: Call<ProductDetailItemResponse>,
                response: Response<ProductDetailItemResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        data.postValue(response.body())
                    } else {
                        Log.d("ERROR CODE", response.code().toString())
                        data.postValue(null)
                    }
                } else {
                    data.postValue(null)
                }
            }

            override fun onFailure(call: Call<ProductDetailItemResponse>, t: Throwable) {
                data.postValue(null)
            }

        })
    }

    //
    fun checkProductOwnedBySeller(accessToken: String, id: Int, isSellerProduct: MutableLiveData<Boolean>) {
        api.getSellerDetailProduct(accessToken, id)
            .enqueue(object : retrofit2.Callback<BuyerProductDetail>{
                override fun onResponse(
                    call: Call<BuyerProductDetail>,
                    response: Response<BuyerProductDetail>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            isSellerProduct.postValue(true)
                        } else {
                            isSellerProduct.postValue(false)
                        }
                    } else {
                        isSellerProduct.postValue(false)
                    }
                }

                override fun onFailure(call: Call<BuyerProductDetail>, t: Throwable) {
                    isSellerProduct.postValue(false)
                }

            })
    }

    // POST ORDER ----------------------------------------------------------------------------------

    // Request Order
    fun postRequestOrder(accessToken: String, request: BuyerOrderRequest)
        = api.postRequestOrder(accessToken, request)

}