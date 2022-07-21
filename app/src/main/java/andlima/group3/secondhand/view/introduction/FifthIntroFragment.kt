package andlima.group3.secondhand.view.introduction

import andlima.group3.secondhand.AuthActivity
import andlima.group3.secondhand.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.local.datastore.UserManager
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FifthIntroFragment : Fragment() {

    private lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fifth_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        GlobalScope.launch {
            userManager.setNotFirstTimeRun()
        }

        val btnLogin: Button = requireView().findViewById(R.id.btn_intro_login)
        val btnRegister: Button = requireView().findViewById(R.id.btn_intro_register)
        val btnSkip: TextView = requireView().findViewById(R.id.btn_intro_skip_auth)

        btnLogin.setOnClickListener {
            requireActivity().startActivity(Intent(requireContext(), AuthActivity::class.java))
        }

        btnRegister.setOnClickListener {
            requireActivity().startActivity(Intent(requireContext(), AuthActivity::class.java))
        }

        btnSkip.setOnClickListener {
            requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
}