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
    var notifLiveDataResponse : MutableLiveData<List<NotificationResponseItem>> = SingleLiveMutableData()
    var notifReadLiveData : MutableLiveData<NotificationResponseItem> = MutableLiveData()







    fun getRegisterLiveDataObserver() : MutableLiveData<String>{
        return registerLiveData
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
    fun notifUserReadLive(token: String, id : Int){
        viewModelScope.launch {
            repository.readNotifRepo(token,id,notifReadLiveData)
        }
    }


    fun notifUserLive(token: String){
        var listT : MutableList<NotificationResponseItem> = mutableListOf()
        var listF : MutableList<NotificationResponseItem> = mutableListOf()
        var listFiltered : MutableList<NotificationResponseItem> = mutableListOf()
        viewModelScope.launch {
            repository.getNotifRepo(token, listT, listF )

            Handler().postDelayed({
                listF.forEach {
                    listFiltered.add(it)
                }
                listT.forEach {
                    listFiltered.add(it)
                }
                notifLiveDataResponse.value = listFiltered
            }, 1200)
        }
    }
    }




