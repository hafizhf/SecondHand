package andlima.group3.secondhand.view

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.showPageLoading2
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.TawaranBuyerAdapter
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.os.Handler
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_info_penawar.*

class InfoPenawarFragment : Fragment() {
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_penawar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBackInfoTawar.setOnClickListener {
            view.findNavController().navigate(R.id.action_infoPenawarFragment2_to_daftarJualFragment2)
        }
        userManager = UserManager(requireContext())

        val orderID = arguments?.getInt("ORDER") as Int
        userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
            getDataPenawar(it, orderID)

            Log.d("AKSES TOKEN", it)
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    fun getPenawarOrders(token: String, id: Int){
        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]


        showPageLoading2(requireView(), true,"Loading")
        viewModel.sellerBuyerOrdersLiveData.observe(viewLifecycleOwner){
            if (it != null){
                val orderAdapter = TawaranBuyerAdapter(viewModel, token, requireContext(), parentFragmentManager, id, requireView())
                orderAdapter.setDataProduk(it)
                orderAdapter.notifyDataSetChanged()
                rv_order_buyer.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rv_order_buyer.adapter = orderAdapter
                showPageLoading2(requireView(), false)
            }

        }
        viewModel.getBuyerOrdersLive(token, id)

    }
    private fun getDataPenawar(token : String, id : Int){
        val viewModel = ViewModelProvider(requireActivity())[SellerViewModel::class.java]
        viewModel.sellerDetailOrdersLiveData.observe(viewLifecycleOwner){
            if (it != null){
                tv_namaPenawar.text = it.user.fullName
                tv_kotaPenawar.text = it.user.city

                if (it.user.imageUrl != null){
                    Glide.with(requireActivity()).load(it.user.imageUrl).apply(
                        RequestOptions()
                    ).into(imagePembeliPenawar)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    getPenawarOrders(token, it.buyerId)
                }, 900)
            }
        }
        viewModel.getDetailOrderLive(token, id)
    }
}