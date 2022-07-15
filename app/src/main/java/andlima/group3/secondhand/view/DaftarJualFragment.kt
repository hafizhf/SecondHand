package andlima.group3.secondhand.view

import andlima.group3.secondhand.MarketApplication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.getDeviceScreenHeight
import andlima.group3.secondhand.func.requireLogin
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.AdapterDaftarJualPager
import andlima.group3.secondhand.view.adapter.AdapterHomePager
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_daftar_jual.*
import kotlinx.android.synthetic.main.fragment_home.*

class DaftarJualFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_jual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userManager = UserManager(requireContext())

        MarketApplication.isConnected.observe(this, { isConnected ->
            val connectionInterfaceHandler: LinearLayout = requireView()
                .findViewById(R.id.dialog_require_internet)

            if (!isConnected) {
                connectionInterfaceHandler.layoutParams.height = getDeviceScreenHeight(requireActivity())
                connectionInterfaceHandler.visibility = View.VISIBLE
            } else {
                connectionInterfaceHandler.visibility = View.GONE

                val requireLoginView: LinearLayout = requireView().findViewById(R.id.dialog_require_login)
                val requireLoginButton: Button = requireView().findViewById(R.id.btn_require_goto_login)
                val isLoggedIn = requireLogin(
                    requireActivity(),
                    requireContext(),
                    userManager,
                    requireLoginView,
                    requireLoginButton
                )

                if (isLoggedIn) {
                    val adapter = AdapterDaftarJualPager(childFragmentManager)
                    viewpager_daftar_jual.adapter = adapter
                    viewpager_daftar_jual.layoutParams.height = getDeviceScreenHeight(requireActivity()) + 100
                    tabs_daftar_jual.setupWithViewPager(viewpager_daftar_jual)
                    adapter.notifyDataSetChanged()

                    userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                        getDataSeller(it)
                        Log.d("AKSES TOKEN", it)
                    }
//                    buttonEdit.setOnClickListener {
//                        view.findNavController().navigate(R.id.action_mainContainerFragment_to_infoAkunFragment)
//                    }
                }
            }
        })
    }


    private fun getDataSeller(token: String) {
        val viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.userDetailLiveData.observe(viewLifecycleOwner){
            if (it != null){
                tv_namaPenjual.text = it.fullName
                tv_kotaPenjual.text = it.city
                Glide.with(this).load(it.imageUrl).into(imagePenjualDaftarJual)

            }else{
                toast(requireContext(), "$token")
            }
        }
        viewModel.userDetailLive(token)


    }


}