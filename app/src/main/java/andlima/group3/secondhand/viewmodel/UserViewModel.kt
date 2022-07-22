package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.SingleLiveEvent.SingleLiveMutableData
import andlima.group3.secondhand.model.history.HistoryResponseItem
import andlima.group3.secondhand.model.notification.NotificationResponseItem
import andlima.group3.secondhand.model.user.UserDetailResponse
import andlima.group3.secondhand.repository.UserRepository
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val repository : UserRepository) : ViewModel(){


    var registerLiveData : MutableLiveData<String> = MutableLiveData()
    var userDetailLiveData : MutableLiveData<UserDetailResponse> = MutableLiveData()

    var notifLiveDataResponse : MutableLiveData<List<NotificationResponseItem>> = SingleLiveMutableData()
    private var notifReadLiveData : MutableLiveData<NotificationResponseItem> = MutableLiveData()

    var historyLiveData : MutableLiveData<List<HistoryResponseItem>> = MutableLiveData()







//    fun getRegisterLiveDataObserver() : MutableLiveData<String>{
//        return registerLiveData
//    }
    fun getHistoryLive(token: String) {
        viewModelScope.launch {
            repository.getHistoryRepo(token, historyLiveData)
        }
    }


    fun registerLiveData(fullName : String, email : String, password : String, phoneNumber : String,address : String, city : String){
        viewModelScope.launch {
            val fullName2 = fullName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val email2 = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val password2 = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneNumber2 =
                phoneNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val address2 = address.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val city2 = city.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            repository.registerRepo(fullName2, email2, password2, phoneNumber2, address2, city2,registerLiveData)
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
        val listT : MutableList<NotificationResponseItem> = mutableListOf()
        val listF : MutableList<NotificationResponseItem> = mutableListOf()
        val listFiltered : MutableList<NotificationResponseItem> = mutableListOf()
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




