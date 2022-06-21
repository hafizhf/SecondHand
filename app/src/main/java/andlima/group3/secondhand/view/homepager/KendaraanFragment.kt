package andlima.group3.secondhand.view.homepager

import andlima.group3.secondhand.MarketApplication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.view.adapter.HomeAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KendaraanFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kendaraan, container, false)
    }

    override fun onResume() {
        super.onResume()
        getProductData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getProductData() {
        val recyclerView : RecyclerView = requireView().findViewById(R.id.rv_home_kendaraan)

        val homeAdapter = HomeAdapter() {
            // On item click
            val selectedID = bundleOf("SELECTED_ID" to it.id)
            Navigation.findNavController(view!!)
                .navigate(R.id.action_mainContainerFragment_to_detailFragment, selectedID)
        }

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = homeAdapter
        MarketApplication.homeFragmentReachedBottom.observe(this, {
            recyclerView.isNestedScrollingEnabled = it
        })

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