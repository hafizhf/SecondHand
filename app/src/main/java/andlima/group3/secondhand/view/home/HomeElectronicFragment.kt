package andlima.group3.secondhand.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.navigateToDetailProduct
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeElectronicFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_electronic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getComputerProducts()
        getPhoneProducts()
        getElectronicProducts()

        actionButtonMore(
            R.id.btn_goto_handphone_more,
            R.id.action_homeElectronicFragment_to_homeResultListFragment,
            98
        )
        actionButtonMore(
            R.id.btn_goto_computer_more,
            R.id.action_homeElectronicFragment_to_homeResultListFragment,
            97
        )
        actionButtonMore(
            R.id.btn_goto_electronic_more_more,
            R.id.action_homeElectronicFragment_to_homeResultListFragment,
            96
        )
    }

    private fun actionButtonMore(textId: Int, navigationId: Int, requestCode: Int = 0) {
        val btnGotoMore: TextView = requireView().findViewById(textId)
        btnGotoMore.setOnClickListener {
            if (requestCode != 0) {
                val code = bundleOf("REQUEST_CODE" to requestCode)
                Navigation.findNavController(view!!).navigate(navigationId, code)
            } else {
                Navigation.findNavController(view!!).navigate(navigationId)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPhoneProducts() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_handphone)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeElectronicFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.phoneProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getPhoneProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getComputerProducts() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_computer)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeElectronicFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.computerProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getComputerProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getElectronicProducts() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_preview_electronic_more)

        val adapter = ProductPreviewAdapter {
            navigateToDetailProduct(it.id, requireView(), R.id.action_homeElectronicFragment_to_detailFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
        viewModel.electronicProducts.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
//                    val a : MutableList<ProductItemResponse> = mutableListOf()
//                    for (i in 0..7) {
//                        a.add(it[i])
//                    }
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        viewModel.getElectronicProducts()
    }
}