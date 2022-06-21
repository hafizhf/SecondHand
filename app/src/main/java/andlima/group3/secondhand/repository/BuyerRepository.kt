package andlima.group3.secondhand.repository

import andlima.group3.secondhand.api.buyer.BuyerApi
import andlima.group3.secondhand.model.home.BuyerProductDetail
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class BuyerRepository @Inject constructor(private val api: BuyerApi) {

    suspend fun getAllProduct() = api.getAllProduct()

    fun getDetailProduct(id: Int, data: MutableLiveData<BuyerProductDetail>) {
        api.getDetailProduct(id).enqueue(object : retrofit2.Callback<BuyerProductDetail>{
            override fun onResponse(
                call: Call<BuyerProductDetail>,
                response: Response<BuyerProductDetail>
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                } else {
                    data.postValue(null)
                }
            }

            override fun onFailure(call: Call<BuyerProductDetail>, t: Throwable) {
                data.postValue(null)
            }

        })
    }
}