package andlima.group3.secondhand.view.splashscreen

import andlima.group3.secondhand.AuthActivity
import andlima.group3.secondhand.MainActivity
import andlima.group3.secondhand.R
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.observeOnce
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.model.login.LoginRequest
import andlima.group3.secondhand.repository.AuthRepository
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: AuthRepository

    // Get data store
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Get something from data store
        userManager = UserManager(this)

        userDataAuth { response, code, message ->
            if (code != -500)
                actionBaseOnResponse(response, code, message)
            else
                splashHandler(MainActivity::class.java)
        }
        transparentStatusBar()
    }

    private fun userDataAuth(action: (response: GetLoginResponse, code: Int, message: String) -> Unit) {
        userManager.emailFlow.asLiveData().observeOnce(this, { email ->
            userManager.passwordFlow.asLiveData().observeOnce(this, { password->
                if (email != "" && password != "") {
                    // When user is logged in, get access token
                    auth.requestLogin(LoginRequest(email, password)) { response, code, message ->
                        action(response, code, message)
                    }
                } else {
                    // When user never login before
                    action(GetLoginResponse("", "", ""), -500, "")
                }
            })
        })
    }

    private fun actionBaseOnResponse(response: GetLoginResponse, code: Int, message: String) {
        when (code) {
            201 -> {
                getAccessToken(response.accessToken)
                splashHandler(MainActivity::class.java)
            }
            401 -> toast(this, "Logged out due to changed on user data")
            500 -> alertDialog(this, "Internal Service Error", message) {
                finish()
            }
            else -> {
                // When user is logged in but device has no connection
                alertDialog(this, "There is no connection", message) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun getAccessToken(accessToken: String) {
        GlobalScope.launch {
            userManager.clearOldAccessTokenPreferences()
            userManager.saveAccessToken(accessToken)
        }
    }

    private fun <T> splashHandler(targetClass: Class<T>) {
        Handler().postDelayed({
            startActivity(Intent(this, targetClass))
            finish()
        }, 3000)
    }

    private fun transparentStatusBar() {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}