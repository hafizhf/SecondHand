package andlima.group3.secondhand.viewmodel


import andlima.group3.secondhand.repository.ProfileRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class ProfileViewModel  @Inject constructor(private val repository : ProfileRepository) : ViewModel() {


    var profileLiveData: MutableLiveData<String> = MutableLiveData()



    fun getProfileLiveDataObserver(): MutableLiveData<String> {
        return profileLiveData
    }


    fun profileLiveData(
        accesstoken : String, nama: String, kota: String, alamat: String, nohp: Int
    ) {
        viewModelScope.launch {
            repository.updateprofile(
                accesstoken, nama, kota, alamat, nohp, profileLiveData)
        }
    }
}