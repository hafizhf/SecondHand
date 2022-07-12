package andlima.group3.secondhand

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MarketApplication: Application() {
    companion object {
        val homeFragmentReachedBottom = MutableLiveData(false)
        val isConnected = MutableLiveData(false)
        val isPreviouslyConnected = MutableLiveData(true)
    }
}