package andlima.group3.secondhand

import andlima.group3.secondhand.view.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val fragment = HomeFragment.newInstane()
//        addFragment(fragment)
    }

    private var content: FrameLayout? = null
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_home -> {
                val fragment = HomeFragment.newInstane()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_notifikasi -> {
                val fragment = NotifikasiFragment.newInstane()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_jual -> {
                val fragment = JualFragment.newInstane()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_daftar_jual -> {
                val fragment = DaftarJualFragment.newInstane()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_akun -> {
                val fragment = AkunFragment.newInstane()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .commit()
    }
}