package andlima.group3.secondhand

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MarketApplication: Application() {
    companion object {
//        @JvmField
        val homeFragmentReachedBottom = MutableLiveData(false)
//        var homeFragmentReachedBottom : Boolean? by Delegates.observable(false) { property, oldValue, newValue ->
//
//        }
    }
}