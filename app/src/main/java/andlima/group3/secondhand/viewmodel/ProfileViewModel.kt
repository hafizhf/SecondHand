package andlima.group3.secondhand.viewmodel


import andlima.group3.secondhand.repository.ProfileRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject




@HiltViewModel
class ProfileViewModel  @Inject constructor(private val repository : ProfileRepository) : ViewModel() {
    
    var profileLiveData: MutableLiveData<String> = MutableLiveData()



    fun getProfileLiveDataObserver(): MutableLiveData<String> {
        return profileLiveData
    }


    fun profileLiveData(
        accesstoken : String, nama: String, kota: String, alamat: String, nohp: Int, image :MultipartBody.Part
    ) {
        viewModelScope.launch {
            val namaa = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), nama)
            val kotaa = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), kota)
            val alamatt = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), alamat)
            val nohpp = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), nohp.toString())



            repository.updateprofile(

                accesstoken, namaa, kotaa, alamatt, nohpp,image, profileLiveData)
        }
    }
}