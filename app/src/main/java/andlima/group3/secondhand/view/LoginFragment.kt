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
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.repository.AuthRepository
import andlima.group3.secondhand.viewmodel.LoginViewModel
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

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

        // Get something from data store
        userManager = UserManager(requireContext())

        login_btn_daftar.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        login_btn_login.setOnClickListener {
            val email = login_et_email.text.toString()
            val password = login_et_password.text.toString()

            if (email != "" && password != "") {
                login(email, password)
            } else {
                Toast.makeText(requireContext(), "Please input all field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.requestLogin(email, password) { response, code, message ->
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