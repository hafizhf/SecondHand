package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.repository.UserRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProdukViewModel : ViewModel(){


    var kategoriData : MutableLiveData<MutableList<String>> = MutableLiveData()


    fun getKategoiObserver() : MutableLiveData<MutableList<String>> {
        return kategoriData
    }


    fun addKategori(nama : String){
        viewModelScope.launch {
            kategoriData.value!!.plus(nama)

        }
    }





}