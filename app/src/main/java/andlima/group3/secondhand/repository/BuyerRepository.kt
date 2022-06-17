package andlima.group3.secondhand.repository

import andlima.group3.secondhand.api.buyer.BuyerApi
import javax.inject.Inject

class BuyerRepository @Inject constructor(private val api: BuyerApi) {
    suspend fun getAllProduct() = api.getAllProduct()
}