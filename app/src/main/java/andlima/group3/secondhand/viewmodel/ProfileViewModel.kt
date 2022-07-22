@file:Suppress("RemoveRedundantCallsOfConversionMethods", "RemoveRedundantCallsOfConversionMethods",
    "RemoveRedundantCallsOfConversionMethods"
)

package andlima.group3.secondhand.viewmodel


import andlima.group3.secondhand.repository.ProfileRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel  @Inject constructor(private val repository : ProfileRepository) : ViewModel() {

    var profileLiveData: MutableLiveData<String> = MutableLiveData()



//    fun getProfileLiveDataObserver(): MutableLiveData<String> {
//        return profileLiveData
//    }


    fun profileLiveData(
        accesstoken : String, nama: String, kota: String, alamat: String, nohp: String, image :MultipartBody.Part
    ) {
        viewModelScope.launch {
            val namaa = nama.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val kotaa = kota.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val alamatt = alamat.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nohpp = nohp.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())



            repository.updateprofile(

                accesstoken, namaa, kotaa, alamatt, nohpp,image, profileLiveData)
        }
    }
}