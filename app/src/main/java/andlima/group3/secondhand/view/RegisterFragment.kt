package andlima.group3.secondhand.view

import andlima.group3.secondhand.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.viewmodel.UserViewModel
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register_btn_daftar.setOnClickListener {
            val namaLengkap = register_et_namalengkap.text.toString()
            val email = register_et_email.text.toString()
            val password =register_et_password.text.toString()
            if (namaLengkap.isNotBlank() && email.isNotBlank() && password.isNotBlank()){
                register(namaLengkap, email, password)
            }
        }
    }
    fun register(namaLengkap : String, email : String, password : String){
        val viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.registerLiveData.observe(requireActivity()){
            when (it) {
                "201" -> {
                    toast(requireContext(), "Register success")
                    //view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
                }
                "400" -> toast(requireContext(), "Email already exist")
                "500" -> toast(requireContext(), "Internal Service Error")
                else -> alertDialog(requireContext(), "Login failed", "No connection") {}
            }
        }
        viewModel.registerLiveData(namaLengkap, email, password, 0, "A", "Surabaya")
    }
}