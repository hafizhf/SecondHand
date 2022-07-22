@file:Suppress("RemoveExplicitTypeArguments")

package andlima.group3.secondhand.view

import andlima.group3.secondhand.MainActivity
import andlima.group3.secondhand.MarketApplication
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.getDeviceScreenHeight
import andlima.group3.secondhand.func.requireLogin
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_akun.*
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

    @SuppressLint("SetTextI18n")
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
                    btn_history.setOnClickListener {
                        view.findNavController().navigate(R.id.action_akunFragment_to_historyFragment)
                    }
                    akun_tv_ubahakun.setOnClickListener {
                        view.findNavController().navigate(R.id.action_akunFragment_to_infoAkunFragment2)
                    }
                    val viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
                    viewModel.userDetailLiveData.observe(viewLifecycleOwner){
                        if (it != null){
                            val isImageLoaded = MutableLiveData<Boolean>(false)

                            if (it.imageUrl != null){
                                Glide.with(requireContext())
                                    .load(it.imageUrl)
                                    .apply(RequestOptions())
                                    .listener(object : RequestListener<Drawable>{
                                        override fun onLoadFailed(
                                            e: GlideException?,
                                            model: Any?,
                                            target: Target<Drawable>?,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            isImageLoaded.postValue(false)
                                            return false
                                        }

                                        override fun onResourceReady(
                                            resource: Drawable?,
                                            model: Any?,
                                            target: Target<Drawable>?,
                                            dataSource: DataSource?,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            isImageLoaded.postValue(true)

                                            return false
                                        }

                                    })
                                    .into(imageAkunSaya)

                            } else {
                                isImageLoaded.postValue(false)
                            }

//                            val profileImage: ImageView = requireView().findViewById(R.id.imageAkunSaya)
//                            val profileBackground: CardView = requireView().findViewById(R.id.cv_background_user_profile)
//
//                            isImageLoaded.observe(this, { imageLoaded ->
//                                if (imageLoaded) {
//                                    setBackgroundProfile(
//                                        requireView(),
//                                        profileImage,
//                                        profileBackground
//                                    )
//                                } else {
//                                    setBackgroundProfile(
//                                        requireView(),
//                                        profileImage,
//                                        profileBackground
//                                    )
//                                }
//                            })

                            requireView().findViewById<TextView>(R.id.tv_user_name_profile)
                                .text = it.fullName
                            requireView().findViewById<TextView>(R.id.tv_user_phone_profile)
                                .text = "+62 ${it.phoneNumber}"
                            requireView().findViewById<TextView>(R.id.tv_user_address_profile)
                                .text = it.address
                            requireView().findViewById<TextView>(R.id.tv_user_city_profile)
                                .text = it.city
                        }
                    }
                    userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
                        viewModel.userDetailLive(it)
                    }

                    btn_goto_setting.setOnClickListener {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_akunFragment_to_settingFragment)
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