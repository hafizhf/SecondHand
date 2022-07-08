package andlima.group3.secondhand.view.daftarjual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.PenawaranAdapter
import andlima.group3.secondhand.view.adapter.TerjualAdapter
import andlima.group3.secondhand.viewmodel.SellerViewModel
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_diminati.*
import kotlinx.android.synthetic.main.fragment_terjual.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TerjualFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TerjualFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_terjual, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userManager = UserManager(requireContext())
        userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
            getAllSold(it)
            Log.d("AKSES TOKEN", it)
        }
    }
    fun getAllSold(token : String){
        val viewModel = ViewModelProvider(requireActivity()).get(SellerViewModel::class.java)
        viewModel.sellerSoldLiveData.observe(viewLifecycleOwner){
            if (it != null){
                val soldAdapter = TerjualAdapter(viewModel,token,viewLifecycleOwner)
                soldAdapter.setDataProduk(it)
                soldAdapter.notifyDataSetChanged()
                rv_terjual_daftarjual.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rv_terjual_daftarjual.adapter = soldAdapter
        }
        }
        viewModel.getSellerSoldProductsLive(token)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TerjualFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TerjualFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}