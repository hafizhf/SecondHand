package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.repository.UserRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val repository : UserRepository) : ViewModel(){


    lateinit var registerLiveData : MutableLiveData<String>


    init {
        registerLiveData = MutableLiveData()
    }
    fun getRegisterLiveDataObserver() : MutableLiveData<String>{
        return registerLiveData
    }


    fun registerLiveData(fullName : String, email : String, password : String, phoneNumber : Int,address : String,image : String){
        viewModelScope.launch {
            repository.registerRepo(fullName, email, password, phoneNumber, address,  image ,registerLiveData)
        }
    }




}