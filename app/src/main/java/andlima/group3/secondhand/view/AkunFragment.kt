package andlima.group3.secondhand.view

import andlima.group3.secondhand.AuthActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.local.datastore.UserManager
import android.content.Intent
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        btn_logout.setOnClickListener {
            alertDialog(requireContext(), "Logout", "Are you sure want to log out?") {
                GlobalScope.launch {
                    userManager.clearDataPreferences()
                }
                requireActivity().startActivity(Intent(requireContext(), AuthActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}