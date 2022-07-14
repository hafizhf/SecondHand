package andlima.group3.secondhand.view

import andlima.group3.secondhand.AuthActivity
import andlima.group3.secondhand.MainActivity
import andlima.group3.secondhand.MarketApplication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.getDeviceScreenHeight

import andlima.group3.secondhand.func.requireLogin
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.viewmodel.SellerViewModel
import andlima.group3.secondhand.viewmodel.UserViewModel

import android.content.Intent
import android.util.Log

import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import kotlinx.android.synthetic.main.fragment_akun.*
import kotlinx.android.synthetic.main.item_terjual.view.*

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AkunFragment : Fragment() {

    // Get data store
    private lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
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
                    akun_tv_ubahakun.setOnClickListener {
                        view.findNavController().navigate(R.id.action_akunFragment_to_infoAkunFragment2)
                    }
                    val viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
                    viewModel.userDetailLiveData.observe(viewLifecycleOwner){
                        if (it != null){
                            if (it.imageUrl != null){
                                Glide.with(requireContext()).load(it.imageUrl).apply(
                                    RequestOptions()
                                ).into(imageAkunSaya)
                            }
                        }
                    }
                    userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                        viewModel.userDetailLive(it)
                    }


                    btn_logout.setOnClickListener {
                        alertDialog(requireContext(), "Logout", "Are you sure want to log out?") {
                            GlobalScope.launch {
                                userManager.clearDataPreferences()
                            }
                            toast(requireContext(), "You are logged out")
                            requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                }
            }
        })
    }
}