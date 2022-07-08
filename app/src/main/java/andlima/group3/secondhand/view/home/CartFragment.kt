package andlima.group3.secondhand.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.navigateToDetailProduct
import andlima.group3.secondhand.func.observeOnce
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

    @SuppressLint("NotifyDataSetChanged")
    private fun getBuyerOrderListData() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.rv_buyer_order_list)

        val adapter = CartAdapter {
            // On item click

        }

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[BuyerViewModel::class.java]
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