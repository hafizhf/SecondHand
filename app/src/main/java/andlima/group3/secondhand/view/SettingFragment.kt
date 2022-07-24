package andlima.group3.secondhand.view

import andlima.group3.secondhand.R
import andlima.group3.secondhand.auth.helper.AuthenticationError
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.snackbarLong
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class SettingFragment : Fragment() {

    private lateinit var userManager: UserManager

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricManager: BiometricManager

    private var isCheckedOnFirst = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val switch: SwitchCompat = requireView().findViewById(R.id.switch_add_biometric_auth)

        userManager = UserManager(requireContext())
        userManager.biometricAuthFlow.asLiveData().observeForever {
            isCheckedOnFirst = it
            switch.isChecked = isCheckedOnFirst
        }
//        isCheckedOnFirst = switch.isChecked

        GlobalScope.launch {
            biometricAuthSwitch()
        }

        val btnGotoAbout : LinearLayout = requireView().findViewById(R.id.btn_goto_tentang)
        btnGotoAbout.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_tentangFragment)
        }
    }

    private suspend fun biometricAuthSwitch() {
        val switch: SwitchCompat = requireView().findViewById(R.id.switch_add_biometric_auth)

        switch.setOnClickListener {
            if (!isCheckedOnFirst) {
                addBiometricAuth(switch)
            } else {
                removeBiometricAuth(requireContext())
            }
        }

        delay(100)
        switch.setOnCheckedChangeListener { _, isChecked ->
            switch.isChecked = isChecked == isCheckedOnFirst
        }
    }

    private fun addBiometricAuth(switch: SwitchCompat) {
        switch.setOnClickListener {
            alertDialog(
                requireContext(),
                "Tambah otentikasi biometrik?",
                "Dengan menambah otentikasi biometrik, anda harus " +
                        "selalu melakukan verifikasi saat membuka aplikasi " +
                        "untuk meningkatkan keamanan." +
                        "\n\nOtentikasi dengan password bisa dilakukan jika " +
                        "kondisi menggunakan biometrik tidak memungkinkan."
            ) {
                setupBiometricAuthentication()
                checkBiometricFeatureState()
                if (isBiometricFeatureAvailable()) {
                    biometricPrompt.authenticate(buildBiometricPrompt())
                }
            }
        }
    }

    private fun removeBiometricAuth(context: Context) {
        alertDialog(
            context,
            "Matikan otentikasi biometrik?",
            "Mematikan otentikasi biometrik akan membuatmu langsung masuk ke aplikasi " +
                    "namun dapat mengurangi keamanan akunmu."
        ) {
            isCheckedOnFirst = false
            GlobalScope.launch {
                userManager.disableBiometricAuth()
            }
            toast(requireContext(), "Otentikasi dimatikan")
        }
    }

    // ADD BIOMETRIC AUTH --------------------------------------------------------------------------

    private fun setupBiometricAuthentication() {
        biometricManager = BiometricManager.from(requireContext())
        val executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor, biometricCallback)
    }

    private fun checkBiometricFeatureState() {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> setErrorNotice("Maaf perangkatmu tidak memiliki sensor biometrik")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> setErrorNotice("Fitur autentikasi biometrik belum bisa digunakan.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> setErrorNotice("Belum ada biometrik yang terdaftar")
            BiometricManager.BIOMETRIC_SUCCESS -> {

            }
            else -> {

            }
        }
    }

    private fun buildBiometricPrompt(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verifikasi identitasmu")
            .setDescription("Biometrik yang digunakan sama dengan yang terdaftar pada perangkatmu")
            .setNegativeButtonText("Batal")
            .setConfirmationRequired(false)
            .build()
    }

    private fun isBiometricFeatureAvailable(): Boolean {
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    private val biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            // Action when success
            isCheckedOnFirst = true
//            requireView().findViewById<SwitchCompat>(R.id.switch_add_biometric_auth).isChecked = true
            GlobalScope.launch {
                userManager.enableBiometricAuth()
            }
            snackbarLong(requireView(), "Otentikasi ditambahkan")
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)

            if (errorCode != AuthenticationError.AUTHENTICATION_DIALOG_DISMISSED.errorCode
                && errorCode != AuthenticationError.CANCELLED.errorCode) {
                setErrorNotice(errString.toString())
            }
        }
    }

    private fun setErrorNotice(errorMessage: String) {
        toast(requireContext(), errorMessage)
    }
}