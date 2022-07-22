package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.kategori.KategoriPilihan
import andlima.group3.secondhand.model.kategori.KategoriResponseItem
import andlima.group3.secondhand.repository.SellerRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProdukViewModel  @Inject constructor(private val repository : SellerRepository)  : ViewModel(){


    var kategoriPilihanLiveData : MutableLiveData<MutableSet<KategoriPilihan>> = MutableLiveData()
    var kategoriLiveData : MutableLiveData<List<KategoriResponseItem>> = MutableLiveData()


    fun getKategoiPilihanObserver() : MutableLiveData<MutableSet<KategoriPilihan>> {
        return kategoriPilihanLiveData

    }


    fun getKategoriLive(){
        repository.getKategoriRepo(kategoriLiveData)
    }





}