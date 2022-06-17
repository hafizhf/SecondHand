package andlima.group3.secondhand.view.homepager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.view.adapter.HomeAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_semua.*

@AndroidEntryPoint
class SemuaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_semua, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProductData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getProductData() {
        val homeAdapter = HomeAdapter() {
            // On item click

        }

        rv_home_semua.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_home_semua.adapter = homeAdapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.allProductData.observe(this, {
            if (it != null) {
                homeAdapter.setProductData(it)
                homeAdapter.notifyDataSetChanged()
            } else {
                // Something to show when there is no product
            }
        })
    }
}