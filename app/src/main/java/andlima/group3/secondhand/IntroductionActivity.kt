package andlima.group3.secondhand

import andlima.group3.secondhand.auth.helper.toast
import andlima.group3.secondhand.view.adapter.IntroPagerAdapter
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class IntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        transparentStatusBar()

        val viewPager: ViewPager = findViewById(R.id.viewpager_intro)
        val tab: TabLayout = findViewById(R.id.tab_intro)
        val adapter = IntroPagerAdapter(supportFragmentManager)

        val btnSkipFirst: TextView = findViewById(R.id.btn_intro_skip_first)
        val btnNextFirst: ImageView = findViewById(R.id.btn_intro_next_first)
        val btnSkip: TextView = findViewById(R.id.btn_intro_skip)
        val btnNext: ImageView = findViewById(R.id.btn_intro_next)

        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager, true)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    tab.visibility = View.GONE
                    btnSkip.visibility = View.GONE
                    btnNext.visibility = View.GONE
                    btnSkipFirst.visibility = View.VISIBLE
                    btnNextFirst.visibility = View.VISIBLE

                    btnNextFirst.setOnClickListener {
                        viewPager.setCurrentItem(1, true)
                    }

                    btnSkipFirst.setOnClickListener {
                        viewPager.setCurrentItem(4, true)
                    }
                }

                if (position in 1..3) {
                    tab.visibility = View.VISIBLE
                    btnSkip.visibility = View.VISIBLE
                    btnNext.visibility = View.VISIBLE
                    btnSkipFirst.visibility = View.GONE
                    btnNextFirst.visibility = View.GONE

                    btnNext.setOnClickListener {
                        viewPager.setCurrentItem(position+1, true)
                    }

                    btnSkip.setOnClickListener {
                        viewPager.setCurrentItem(4, true)
                    }
                }

                if (position == 4) {
                    tab.visibility = View.GONE
                    btnSkip.visibility = View.GONE
                    btnNext.visibility = View.GONE
                    btnSkipFirst.visibility = View.GONE
                    btnNextFirst.visibility = View.GONE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun transparentStatusBar() {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT

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