package andlima.group3.secondhand.view

import andlima.group3.secondhand.MarketApplication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.viewmodel.ProfileViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_info_akun.*

class InfoAkunFragment : Fragment() {
    lateinit var userManager: UserManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userManager = UserManager(requireContext())
//        infoAkun_btn_Simpan.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_infoAkunFragment_to_AkunFragment)
//        }

        infoAkun_btn_Simpan.setOnClickListener {
            val nama = infoAkun_et_nama.text.toString()
            val kota = infoAkun_et_kota.text.toString()
            val alamat = infoAkun_et_alamat.text.toString()
            val nohp = infoAkun_et_nohp.text.toString().toInt()
            if (nama.isNotBlank() && kota.isNotBlank() && alamat.isNotBlank()){
                profile(nama,kota,alamat,nohp)
            }
        }
    }
    fun profile(nama : String, kota : String, alamat : String, nohp : Int){
        val viewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        viewModel.profileLiveData.observe(requireActivity()){
            when (it) {
                "200" -> {
                    toast(requireContext(), "Update Profille success")
//                    requireActivity().onBackPressedDispatcher.addCallback(this, MarketApplication.onBackPressedCallback)
                    parentFragmentManager.popBackStack()
                }
                "400" -> toast(requireContext(), "Email already used")
                "500" -> alertDialog(requireContext(), "Internal server error", "Try again later") {}
                else -> alertDialog(requireContext(), "Update failed", "No connection") {}
            }
        }
        userManager.accessTokenFlow.asLiveData().observe(viewLifecycleOwner){
            viewModel.profileLiveData(it, nama, kota, alamat,nohp)
        }

    }
}