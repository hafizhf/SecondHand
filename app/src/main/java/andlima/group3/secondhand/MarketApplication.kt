package andlima.group3.secondhand

import android.app.Application
import androidx.activity.OnBackPressedCallback
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

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back button event
            }
        }
    }
}