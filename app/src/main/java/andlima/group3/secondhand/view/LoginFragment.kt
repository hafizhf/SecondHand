package andlima.group3.secondhand.view

import andlima.group3.secondhand.MainActivity
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.showPageLoading
import andlima.group3.secondhand.func.showPassword
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.viewmodel.LoginViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val targetEditText: EditText = requireView().findViewById(R.id.login_et_password)
        val btnShowPassword: ImageView = requireView().findViewById(R.id.btn_show_pwd)
        btnShowPassword.setOnClickListener {
            showPassword(targetEditText, btnShowPassword)
        }

        login_arrowback.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
//        login_arrowback.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
//        }

        // Get something from data store
        userManager = UserManager(requireContext())

        login_btn_daftar.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        login_btn_login.setOnClickListener {
            val email = login_et_email.text.toString()
            val password = login_et_password.text.toString()

            if (email != "" && password != "") {
                showPageLoading(requireView(), true, "Logging In")
                login(email, password)
            } else {
                Toast.makeText(requireContext(), "Please input all field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.requestLogin(email, password) { response, code, message ->
            showPageLoading(requireView(), false)
            when (code) {
                201 -> {
                    saveLoginResponse(response, password)
                    toast(requireContext(), "Login success")
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                401 -> toast(requireContext(), message)
                500 -> alertDialog(requireContext(), "Internal Service Error", message) {}
                else -> alertDialog(requireContext(), "Login failed", "No connection") {}
            }
        }
    }

    private fun saveLoginResponse(response: GetLoginResponse, password: String) {
        GlobalScope.launch {
            userManager.saveAccessToken(response.accessToken)
            userManager.saveUserData(
                response.name,
                response.email,
                password
            )
        }
    }
}