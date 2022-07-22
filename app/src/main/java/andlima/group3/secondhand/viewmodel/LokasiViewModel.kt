package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.lokasi.KotaResponse
import andlima.group3.secondhand.model.lokasi.ProvinsiResponse
import andlima.group3.secondhand.repository.LokasiRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LokasiViewModel @Inject constructor(private val repository : LokasiRepository) : ViewModel() {
    var provinsiLiveData : MutableLiveData<ProvinsiResponse> = MutableLiveData()
    var kotaLiveData : MutableLiveData<KotaResponse> = MutableLiveData()

    fun getProvinsi(){
        viewModelScope.launch {
            repository.provinsiRepo(provinsiLiveData)
        }
    }
    fun getKota(id : Int){
        viewModelScope.launch {
            repository.kotaRepo(id, kotaLiveData)
        }
    }


}