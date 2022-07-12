package andlima.group3.secondhand

import andlima.group3.secondhand.func.colorList
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.services.ConnectionStatus
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Integrate navigation component and bottom navigation
        val navController : NavController = Navigation.findNavController(this, R.id.main_fragment_container)
        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Bottom navigation visibility handler
        val navHostFragment : NavHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navHostFragment.childFragmentManager.addOnBackStackChangedListener {
            hideBottomNavigationForSubPage(bottomNavigationView)
        }

        // Set global variable of connection status, so everything can observe with only one register
        ConnectionStatus(this).observe(this, {
            MarketApplication.isConnected.postValue(it)
            MarketApplication.isPreviouslyConnected.postValue(!it)
        })

        // Make status bar transparent
        transparentStatusBar()

        checkInternetConnection(this)
    }

    @SuppressLint("SetTextI18n")
    private fun checkInternetConnection(lifecycleOwner: LifecycleOwner) {
        var oldStatus = true

        MarketApplication.isConnected.observe(this, {
            val containerView: CardView = findViewById(R.id.cv_connection_container)
            val status: TextView = findViewById(R.id.tv_connection_information)

            if (it) {
                if (!oldStatus) {
                    containerView.setCardBackgroundColor(this.colorList(this, R.color.second_hand_success))
                    status.text = "Connected"
                    Handler(Looper.getMainLooper()).postDelayed({
                        containerView.visibility = View.GONE
                    }, 3000)
                }
            } else {
                containerView.visibility = View.VISIBLE
                containerView.setCardBackgroundColor(this.colorList(this, R.color.second_hand_danger))
                status.text = "No connection"
            }
            oldStatus = it
        })
    }

    private fun hideBottomNavigationForSubPage(bottomNavigationView : BottomNavigationView) {
        val navHostFragment : NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val showedFragment = navHostFragment.childFragmentManager.fragments[0].toString()

        if (
            "HomeFragment" !in showedFragment
            && "NotifikasiFragment" !in showedFragment
//            && "JualFragment" !in showedFragment
            && "DaftarJualFragment" !in showedFragment
            && "AkunFragment" !in showedFragment
        ) {
            bottomNavigationView.visibility = View.GONE
        } else {
            bottomNavigationView.visibility = View.VISIBLE
        }
    }

    private fun transparentStatusBar() {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT

//        val view = window.decorView
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}