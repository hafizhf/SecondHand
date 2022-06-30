package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.SingleLiveEvent.SingleLiveMutableData
import andlima.group3.secondhand.model.notification.NotifData
import andlima.group3.secondhand.model.notification.NotificationResponseItem
import andlima.group3.secondhand.model.produk.ProductResponse
import andlima.group3.secondhand.model.user.UserDetailResponse
import andlima.group3.secondhand.repository.UserRepository
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val repository : UserRepository) : ViewModel(){


    var registerLiveData : MutableLiveData<String> = MutableLiveData()
    var userDetailLiveData : MutableLiveData<UserDetailResponse> = MutableLiveData()
    var notifLiveDataProduct : SingleLiveMutableData<List<ProductResponse>> = SingleLiveMutableData()
    var notifLiveDataResponse : SingleLiveMutableData<List<NotificationResponseItem>> = SingleLiveMutableData()
    var notifLiveDataFix : SingleLiveMutableData<List<NotifData>> = SingleLiveMutableData()






    fun getRegisterLiveDataObserver() : MutableLiveData<String>{
        return registerLiveData
    }
    fun setGabunganNotif(listGabungan: MutableList<NotifData>){
        notifLiveDataFix.value = listGabungan
    }


    fun registerLiveData(fullName : String, email : String, password : String, phoneNumber : Int,address : String, city : String){
        viewModelScope.launch {
            repository.registerRepo(fullName, email, password, phoneNumber, address, city ,registerLiveData)
        }
    }
    fun userDetailLive(token : String){

        viewModelScope.launch {
            repository.getDetailUser(token, userDetailLiveData)
        }
    }
    fun getDetailProdukLive(token: String , listId : MutableList<Int>){

        viewModelScope.launch {
            repository.getDetailProduk(token, listId, notifLiveDataProduct)

        }


    }
    fun notifUserLive(token: String){
//        viewModelScope.launch {
//            val listNotif = repository.getNotifRepo(token,notifLiveDataFix)
//            if (listNotif != null){
//                listNotif.forEach{
//                    getDetailProdukLive(token, it.productId)
//                }
//            }
        viewModelScope.launch {
            var listT : MutableList<NotificationResponseItem>? = mutableListOf()
            var listF : MutableList<NotificationResponseItem>? = mutableListOf()
            var listFiltered : MutableList<NotificationResponseItem> = mutableListOf()
            var dataProduct : MutableList<Int> = mutableListOf()

            repository.getNotifRepo(token, listT, listF)
            Handler().postDelayed({
                Log.d("FALSEEE", listT.toString())
                Log.d("TRUEEE", listF.toString())
                if (listT != null && listF != null) {
                    listF.forEach {
                        listFiltered.add(it)
                    }
                    listT.forEach {
                        listFiltered.add(it)
                    }

                }

                listFiltered.forEach {

                    dataProduct.add(it.productId)

                }
                getDetailProdukLive(token, dataProduct )

                notifLiveDataResponse.value = listFiltered





            }, 3000)





        }

        }



    }




