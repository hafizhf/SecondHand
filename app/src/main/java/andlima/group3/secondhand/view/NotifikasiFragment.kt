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
import andlima.group3.secondhand.model.kategori.KategoriPilihan
import andlima.group3.secondhand.model.notification.NotifData
import andlima.group3.secondhand.model.notification.NotificationResponseItem
import andlima.group3.secondhand.model.produk.ProductResponse
import andlima.group3.secondhand.view.adapter.DaftarJualAdapter
import andlima.group3.secondhand.view.adapter.NotifikasiAdapter
import andlima.group3.secondhand.view.adapter.PenawaranAdapter
import andlima.group3.secondhand.viewmodel.SellerViewModel
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_diminati.*
import kotlinx.android.synthetic.main.fragment_notifikasi.*
import kotlinx.android.synthetic.main.fragment_produk.*

class NotifikasiFragment : Fragment() {
    lateinit var userManager: UserManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_notifikasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userManager = UserManager(requireContext())

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
                    userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                        getNotifs(it)
                    }
                }
            }
        })
    }

    fun getNotifs(token : String){
        val viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.notifLiveDataResponse.observe(viewLifecycleOwner){

            if (it.isNotEmpty()){
                val notifAdapter = NotifikasiAdapter{
                    if (it.read.equals(false)){
                        viewModel.notifUserReadLive(token, it.id)
                        getNotifs(token)

                    }
                    if (it.status == "create"){
                        val selectedID = bundleOf("SELECTED_ID" to it.productId)
                        view?.findNavController()
                            ?.navigate(R.id.action_notifikasiFragment_to_detailFragment, selectedID)

                    }else {
//                            val selectedID = bundleOf("SELECTED_ID" to it., "ORDER" to it.id)
//                            view?.findNavController()
//                                ?.navigate(R.id.action_daftarJualFragment_to_infoPenawarFragment2, selectedID)
                    }

                }
                notifAdapter.setDataNotif(it)
                notifAdapter.notifyDataSetChanged()
                rv_notif.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rv_notif.adapter = notifAdapter
            }else{


            }
        }
        viewModel.notifUserLive(token)
    }
}