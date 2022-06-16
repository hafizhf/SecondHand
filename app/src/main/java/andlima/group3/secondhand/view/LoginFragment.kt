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
import andlima.group3.secondhand.repository.AuthRepository
import andlima.group3.secondhand.viewmodel.LoginViewModel
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    toast(requireContext(), "Login success")
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                401 -> toast(requireContext(), message)
                500 -> alertDialog(requireContext(), "Internal Service Error", message) {}
                else -> alertDialog(requireContext(), "Login failed", "Unknown") {}
            }
        }
    }
}