package andlima.group3.secondhand.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.requireLogin
import andlima.group3.secondhand.local.datastore.UserManager
import android.widget.Button
import android.widget.LinearLayout

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

        val requireLoginView: LinearLayout = requireView().findViewById(R.id.dialog_require_login)
        val requireLoginButton: Button = requireView().findViewById(R.id.btn_require_goto_login)
        requireLogin(requireContext(), userManager, requireLoginView, requireLoginButton)
    }

    companion object {
        fun newInstane(): NotifikasiFragment {
            val fragment = NotifikasiFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}