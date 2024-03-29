@file:Suppress("NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter",
    "NestedLambdaShadowedImplicitParameter", "NestedLambdaShadowedImplicitParameter"
)

package andlima.group3.secondhand.view.daftarjual

import andlima.group3.secondhand.R
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.view.adapter.EmptyAdapter
import andlima.group3.secondhand.view.adapter.PenawaranAdapter
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_diminati.*

class DiminatiFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diminati, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userManager = UserManager(requireContext())
        userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
            getAllOrders(it)
            Log.d("AKSES TOKEN", it)
            diminatiSwipe.setOnRefreshListener {
                getAllOrders(it)
                diminatiSwipe.isRefreshing = false
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllOrders(token: String) {
        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        val listFiltered : MutableList<SellerOrdersItem> = mutableListOf()

        viewModel.sellerOrdersLiveData.observe(viewLifecycleOwner){
            listFiltered.clear()
            if (it != null){
                if (it.isNotEmpty()){
                    Log.d("DIMINATI", it.toString())
                    val orderAdapter = PenawaranAdapter {
                        val selectedID = bundleOf("SELECTED_ID" to it.buyerId, "ORDER" to it.id)
                        view?.findNavController()
                            ?.navigate(R.id.action_daftarJualFragment_to_infoPenawarFragment2, selectedID)

                    }
                    it.forEach {
                        if (it.status == "pending" || it.status == "terima"){
                            listFiltered.add(it)
                        }
                        Log.d("DIMINATI2", it.toString())

                    }
                    if (listFiltered.isEmpty()){
                        val emptyAdapter = EmptyAdapter()
                        emptyAdapter.setDataProduk(listOf("asdasd"))
                        rv_order_daftarjual.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        rv_order_daftarjual.adapter = emptyAdapter
                        gambarDiminati.visibility = View.VISIBLE

                    }else{
                        gambarDiminati.visibility = View.GONE

                    }

                    orderAdapter.setDataProduk(listFiltered)
                    orderAdapter.notifyDataSetChanged()
                    rv_order_daftarjual.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    rv_order_daftarjual.adapter = orderAdapter
                }else{
                    gambarDiminati.visibility = View.VISIBLE




                }

            }
        }
        viewModel.getSellerAllOrdersLive(token)

    }
}