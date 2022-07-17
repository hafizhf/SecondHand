package andlima.group3.secondhand.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.HistoryAdapter
import andlima.group3.secondhand.view.adapter.TerjualAdapter
import andlima.group3.secondhand.viewmodel.SellerViewModel
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_daftar_jual.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_terjual.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userManager = UserManager(requireContext())
        val viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
            getHistory(it, viewModel)
            getDataSeller(it, viewModel)
            Log.d("AKSES TOKEN", it)
        }

    }
    private fun getDataSeller(token: String, viewModel: UserViewModel) {
        viewModel.userDetailLiveData.observe(viewLifecycleOwner){
            if (it != null){
                txtNamaPenggunaHistory.text = it.fullName
                txtKotaPenggunaHistory.text = it.city
                Glide.with(this).load(it.imageUrl).into(imageHistoriAkun)

            }else{
                toast(requireContext(), "$token")
            }
        }
        viewModel.userDetailLive(token)


    }
    fun getHistory(token : String, viewModel: UserViewModel){
        viewModel.historyLiveData.observe(viewLifecycleOwner){
            if (it != null){
                val historyAdapter = HistoryAdapter()
                historyAdapter.setDataProduk(it)
                historyAdapter.notifyDataSetChanged()
                rv_history.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rv_history.adapter = historyAdapter
            }
        }
        viewModel.getHistoryLive(token)

    }


}