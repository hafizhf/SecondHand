package andlima.group3.secondhand.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.navigateToDetailProduct
import andlima.group3.secondhand.func.observeOnce
import andlima.group3.secondhand.func.snackbarLong
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.home.newhome.ProductItemResponse
import andlima.group3.secondhand.view.adapter.CartAdapter
import andlima.group3.secondhand.view.adapter.ProductPreviewAdapter
import andlima.group3.secondhand.viewmodel.BuyerViewModel
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    lateinit var userManager: UserManager
    lateinit var adapter : CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())

        getBuyerOrderListData()
    }

    override fun onResume() {
        super.onResume()
        getBuyerOrderListData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBuyerOrderListData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_buyer_order_list)
        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]

        adapter = CartAdapter { code, orderId ->
            // On item click
            when (code) {
                // Edit
                1 -> {

                }
                // Delete
                2 -> {
                    viewModel.deleteResponse.observe(this, {
                        if (it != null) {
                            snackbarLong(requireView(), "Tawaran berhasil dibatalkan")
                        } else {
                            toast(requireContext(), "Tawaran gagal dibatalkan")
                        }
                    })
                    userManager.accessTokenFlow.asLiveData().observeOnce(this, {
                        viewModel.deleteOrder(it, orderId)
                        viewModel.getBuyerOrderList(it)
                        adapter.notifyDataSetChanged()
                    })
                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = adapter

        viewModel.orderList.observe(this, {
            if (it != null) {
                if (it.isNotEmpty()) {
                    adapter.setProductData(it)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        userManager.accessTokenFlow.asLiveData().observeOnce(this, {
            viewModel.getBuyerOrderList(it)
        })
    }
}