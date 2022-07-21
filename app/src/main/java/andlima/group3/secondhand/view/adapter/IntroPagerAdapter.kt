package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.view.introduction.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class IntroPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val pages = listOf(
        FirstIntroFragment(),
        SecondIntroFragment(),
        ThirdIntroFragment(),
        FourthIntroFragment(),
        FifthIntroFragment()
    )

    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }
}