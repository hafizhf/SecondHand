package andlima.group3.secondhand.view.daftarjual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.DaftarJualAdapter
import andlima.group3.secondhand.view.adapter.HomeAdapter
import andlima.group3.secondhand.viewmodel.SellerViewModel
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_produk.*
import kotlinx.android.synthetic.main.fragment_semua.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProdukFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProdukFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_produk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userManager = UserManager(requireContext())
        userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
            getAllProducts(it)
            Log.d("AKSES TOKEN", it)
        }
    }

    private fun getAllProducts(token: String) {
        val viewModel = ViewModelProvider(requireActivity()).get(SellerViewModel::class.java)
        viewModel.sellerProductsLiveData.observe(viewLifecycleOwner){
            if (it != null){
                val produkAdapter = DaftarJualAdapter {
                    val selectedID = bundleOf("SELECTED_ID" to it.id)
                    view?.findNavController()
                        ?.navigate(R.id.action_mainContainerFragment_to_detailFragment, selectedID)


                }
                produkAdapter.setDataProduk(it)
                produkAdapter.notifyDataSetChanged()
                rv_produk_daftarjual.layoutManager = GridLayoutManager(requireContext(), 2)
                rv_produk_daftarjual.adapter = produkAdapter
            }
        }
        viewModel.getSellerAllProductsLive(token)


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProdukFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProdukFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}