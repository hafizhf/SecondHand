@file:Suppress("PrivatePropertyName")

package andlima.group3.secondhand

import andlima.group3.secondhand.auth.helper.AuthenticationError
import andlima.group3.secondhand.auth.helper.navigateTo
import andlima.group3.secondhand.auth.helper.toast
import andlima.group3.secondhand.func.observeOnce
import andlima.group3.secondhand.local.datastore.UserManager
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import java.security.KeyStore
import javax.crypto.KeyGenerator

class BiometricAuthActivity : AppCompatActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricManager: BiometricManager
    private lateinit var userManager: UserManager

    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private var KEY_NAME = "default_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric_auth)

        userManager = UserManager(this)

        setupBiometricAuthentication()
        checkBiometricFeatureState()
        if (isBiometricFeatureAvailable()) {
            biometricPrompt.authenticate(buildBiometricPrompt())
        }

        val biometricLayout: LinearLayout = findViewById(R.id.layout_verify_with_biometric)
        val passwordLayout: LinearLayout = findViewById(R.id.layout_verify_with_password)
        val btnBiometricAuth: Button = findViewById(R.id.iv_authenticate)
        val btnPasswordAuth: Button = findViewById(R.id.btn_use_password)
        val btnVerifyWithPassword: Button = findViewById(R.id.btn_verify)
        val etPassword: EditText = findViewById(R.id.et_password_verify)

        btnPasswordAuth.setOnClickListener {
            biometricLayout.visibility = View.GONE
            passwordLayout.visibility = View.VISIBLE
        }

        btnBiometricAuth.setOnClickListener {
            if (isBiometricFeatureAvailable()) {
                biometricPrompt.authenticate(buildBiometricPrompt())
            }
        }

        btnVerifyWithPassword.setOnClickListener {
            val password = etPassword.text.toString()

            if (password != "") {
                userManager.passwordFlow.asLiveData().observeOnce(this, {
                    if (password == it) {
                        navigateTo<MainActivity>()
                    } else {
                        toast("Password salah")
                    }
                })
            } else {
                toast("Isi kolom dengan password akunmu")
            }
        }
    }

    private fun setupBiometricAuthentication() {
        biometricManager = BiometricManager.from(this)
        val executor = ContextCompat.getMainExecutor(this)
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
            .setDescription("Konfirmasi identitasmu dengan fingerprint yang terdaftar")
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
            navigateTo<MainActivity>()
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
        toast(errorMessage)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun validateFingerPrint() {
        // Generate Key

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
            keyStore.load(null)
            keyGenerator.init(KeyGenParameterSpec.Builder(KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
            )
        } catch (e:Exception) {}
    }
}