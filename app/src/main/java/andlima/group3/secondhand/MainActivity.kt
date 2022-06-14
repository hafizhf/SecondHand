package andlima.group3.secondhand

import andlima.group3.secondhand.view.*
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transparentStatusBar()
        bottomNavigationInteraction()
    }

    private fun bottomNavigationInteraction() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.isSelected

        bottom_navigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_notifikasi -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, NotifikasiFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_jual -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, JualFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_daftar_jual -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, DaftarJualFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_akun -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, AkunFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
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