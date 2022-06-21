package andlima.group3.secondhand.view

import andlima.group3.secondhand.MarketApplication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.getDeviceScreenHeight
import andlima.group3.secondhand.func.isScrollReachedBottom
import andlima.group3.secondhand.view.adapter.AdapterHomePager
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AdapterHomePager(childFragmentManager)
        viewpager_home.adapter = adapter
        viewpager_home.layoutParams.height = getDeviceScreenHeight(requireActivity()) + 100
        tabs_home.setupWithViewPager(viewpager_home)
        adapter.notifyDataSetChanged()
        retainInstance = true

//        tabs_home.getTabAt(0)!!.setIcon(R.drawable.ic_fi_search)

        isScrollReachedBottom(scroll_view_home) {
            MarketApplication.homeFragmentReachedBottom.value = it
        }
    }
}