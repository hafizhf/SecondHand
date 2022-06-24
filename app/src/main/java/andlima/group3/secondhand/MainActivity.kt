package andlima.group3.secondhand

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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

        // Make status bar transparent
        transparentStatusBar()
    }

    private fun hideBottomNavigationForSubPage(bottomNavigationView : BottomNavigationView) {
        val navHostFragment : NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val showedFragment = navHostFragment.childFragmentManager.fragments[0].toString()

        if (
            "HomeFragment" !in showedFragment
            && "NotifikasiFragment" !in showedFragment
            && "JualFragment" !in showedFragment
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