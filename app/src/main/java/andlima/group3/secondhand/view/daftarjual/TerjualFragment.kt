package andlima.group3.secondhand.view.daftarjual

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.showEmptyListSign
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.TerjualAdapter
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
import kotlinx.android.synthetic.main.fragment_produk.*
import kotlinx.android.synthetic.main.fragment_terjual.*

class TerjualFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terjual, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userManager = UserManager(requireContext())
        userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
            getAllSold(it)
            Log.d("AKSES TOKEN", it)
            terjualSwipe.setOnRefreshListener {
                getAllSold(it)
                terjualSwipe.isRefreshing = false
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun getAllSold(token : String){
        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        viewModel.sellerSoldLiveData.observe(viewLifecycleOwner){
            if (it != null){
                if (it.isNotEmpty()){
                    val soldAdapter = TerjualAdapter(
                        { sellerOrderItem ->
                            val selectedID = bundleOf("SELECTED_ID" to sellerOrderItem.buyerId, "ORDER" to sellerOrderItem.id)
                            view?.findNavController()
                                ?.navigate(R.id.action_daftarJualFragment_to_infoPenawarFragment2, selectedID)},
                        viewModel,token,viewLifecycleOwner)
                    soldAdapter.setDataProduk(it)
                    soldAdapter.notifyDataSetChanged()
                    rv_terjual_daftarjual.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    rv_terjual_daftarjual.adapter = soldAdapter
                    gambarTerjual.visibility = View.GONE

                }else{
                    gambarTerjual.visibility = View.VISIBLE

                }

        }
        }
        viewModel.getSellerSoldProductsLive(token)

    }
}