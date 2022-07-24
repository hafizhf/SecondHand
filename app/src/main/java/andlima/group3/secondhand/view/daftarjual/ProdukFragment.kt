@file:Suppress("NestedLambdaShadowedImplicitParameter")

package andlima.group3.secondhand.view.daftarjual

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.showEmptyListSign
import andlima.group3.secondhand.func.showPageLoading2
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.DaftarJualAdapter
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
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_diminati.*
import kotlinx.android.synthetic.main.fragment_produk.*

class ProdukFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_produk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userManager = UserManager(requireContext())
        userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
            getAllProducts(it)
            Log.d("AKSES TOKEN", it)
            produkSwipe.setOnRefreshListener {
                getAllProducts(it)
                produkSwipe.isRefreshing = false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllProducts(token: String) {
        showPageLoading2(requireView(), true)

        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        viewModel.sellerProductsLiveData.observe(viewLifecycleOwner){
            if (it != null){
                if (it.isNotEmpty()){
                    gambarProduk.visibility = View.GONE

                    val produkAdapter = DaftarJualAdapter {
                        val selectedID = bundleOf("SELECTED_ID" to it.id)
                        view?.findNavController()
                            ?.navigate(R.id.action_daftarJualFragment_to_detailFragment, selectedID)
                    }
                    produkAdapter.setDataProduk(it)
                    produkAdapter.notifyDataSetChanged()
                    rv_produk_daftarjual.layoutManager = GridLayoutManager(requireContext(), 2)
                    rv_produk_daftarjual.adapter = produkAdapter
                    showPageLoading2(requireView(), false)
                }else{
                    showPageLoading2(requireView(), false)
                    gambarProduk.visibility = View.VISIBLE

                }


            }
//            else{
//
//            }
        }
        viewModel.getSellerAllProductsLive(token)
    }
}