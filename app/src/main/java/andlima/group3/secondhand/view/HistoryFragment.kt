package andlima.group3.secondhand.view

import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.HistoryAdapter
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userManager = UserManager(requireContext())
        val viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

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
                toast(requireContext(), token)
            }
        }
        viewModel.userDetailLive(token)


    }
    @SuppressLint("NotifyDataSetChanged")
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