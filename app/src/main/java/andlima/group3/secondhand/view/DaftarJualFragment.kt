package andlima.group3.secondhand.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.view.adapter.AdapterDaftarJualPager
import andlima.group3.secondhand.view.adapter.AdapterHomePager
import kotlinx.android.synthetic.main.fragment_daftar_jual.*
import kotlinx.android.synthetic.main.fragment_home.*

class DaftarJualFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_jual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewpager_daftar_jual.adapter = AdapterDaftarJualPager(parentFragmentManager)
        tabs_daftar_jual.setupWithViewPager(viewpager_daftar_jual)
    }


}