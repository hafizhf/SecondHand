package andlima.group3.secondhand.view.adapter

import andlima.group3.secondhand.view.daftarjual.DiminatiFragment
import andlima.group3.secondhand.view.daftarjual.ProdukFragment
import andlima.group3.secondhand.view.daftarjual.TerjualFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AdapterDaftarJualPager(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val pages = listOf(
        ProdukFragment(),
        DiminatiFragment(),
        TerjualFragment()
    )


    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            0 -> "Produk"
            1 -> "Diminati"
            2 -> "Terjual"
            else -> "N/A"
        }
    }
}