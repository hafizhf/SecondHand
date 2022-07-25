@file:Suppress("ReplaceCallWithBinaryOperator", "NestedLambdaShadowedImplicitParameter")

package andlima.group3.secondhand.view

import andlima.group3.secondhand.MarketApplication
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.getDeviceScreenHeight
import andlima.group3.secondhand.func.requireLogin
import andlima.group3.secondhand.func.showPageLoading
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.view.adapter.NotifikasiAdapter
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_notifikasi.*

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
                        showPageLoading(requireView(), true)
                        getNotifs(it)
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getNotifs(token : String){
        val viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        viewModel.notifLiveDataResponse.observe(viewLifecycleOwner){
            showPageLoading(requireView(), false)
            if (it.isNotEmpty()){
                val notifAdapter = NotifikasiAdapter{

                        if (it.read.equals(false)){
                            viewModel.notifUserReadLive(token, it.id)
                            getNotifs(token)
                        }
                    if (it.product != null){
                        if (it.status == "create"){
                            val selectedID = bundleOf("SELECTED_ID" to it.productId)
                            view?.findNavController()
                                ?.navigate(R.id.action_notifikasiFragment_to_detailFragment, selectedID)
                        }else {
                            if (it.notificationType == "seller"){

                                val orderID = bundleOf("ORDER" to it.orderId)
                                view?.findNavController()?.navigate(R.id.action_notifikasiFragment_to_infoPenawarFragment2, orderID)
                            }else{
                                val selectedID = bundleOf("SELECTED_ID" to it.productId)
                                view?.findNavController()
                                    ?.navigate(R.id.action_notifikasiFragment_to_detailFragment, selectedID)
                            }


                        }
                    }



                }
                notifAdapter.setDataNotif(it)
                notifAdapter.notifyDataSetChanged()
                rv_notif.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rv_notif.adapter = notifAdapter
            }
        }
        viewModel.notifUserLive(token)
    }
}