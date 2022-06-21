package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.view.homepager.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AdapterHomePager(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val pages = listOf(
        SemuaFragment(),
        HobiFragment(),
        KendaraanFragment(),
        AksesorisFragment(),
        ElektronikFragment(),
        KesehatanFragment()
    )

    fun gerPages() = pages


    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            0 -> "Semua"
            1 -> "Hobi"
            2 -> "Kendaraan"
            3 -> "Aksesoris"
            4 -> "Elektronik"
            5 -> "Kesehatan"
            else -> "N/A"
        }
    }
}