package andlima.group3.secondhand.view.splashscreen

import andlima.group3.secondhand.*
import andlima.group3.secondhand.func.alertDialog
import andlima.group3.secondhand.func.observeOnce
import andlima.group3.secondhand.func.toast
import andlima.group3.secondhand.local.datastore.UserManager
import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.model.login.LoginRequest
import andlima.group3.secondhand.repository.AuthRepository
import andlima.group3.secondhand.services.ConnectionStatus
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
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

    private val hasConnection = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setWindowFullScreen()
        checkConnectionFirst()

        // Get something from data store
        userManager = UserManager(this)

        Log.d("DISINI", "0")
        userManager.firstTimeFlow.asLiveData().observeOnce(this, { firstTime ->
            Log.d("DISINI", "1")

            if (firstTime) {
                Log.d("DISINI", "2")

                MarketApplication.isConnected.observe(this, { isConnected ->

                    if (isConnected) {

                        Log.d("DISINI", "3")
//                        GlobalScope.launch {
//                            userManager.setNotFirstTimeRun()
//                        }
//                        continueToMainActivity()
                        splashHandler(IntroductionActivity::class.java)

                    } else {

                        Log.d("DISINI", "4")
                        alertDialog(
                            this,
                            "You're not connected",
                            "Please connect to internet on your first run"
                        ) {
                            finish()
                        }

                    }

                })

            } else {
                Log.d("DISINI", "5")
                continueToMainActivity()
            }
            Log.d("DISINI", "6")
        })
    }

    private fun continueToMainActivity() {
        MarketApplication.isConnected.observeOnce(this, { isConnected ->
            Log.d("DISINI", "7")
            if (isConnected) {
                userDataAuth { response, code, message ->
                    if (code != -500)
                        actionBaseOnResponse(response, code, message)
                    else {
                        userManager.biometricAuthFlow.asLiveData().observeOnce(this, {
                            if (it) {
                                splashHandler(BiometricAuthActivity::class.java)
                            } else {
                                splashHandler(MainActivity::class.java)
                            }
                        })
                    }
                }
            } else {
                userManager.biometricAuthFlow.asLiveData().observeOnce(this, {
                    if (it) {
                        splashHandler(BiometricAuthActivity::class.java)
                    } else {
                        splashHandler(MainActivity::class.java)
                    }
                })
            }
        })
    }

    private fun checkConnectionFirst() {
        ConnectionStatus(this).observe(this, {
            MarketApplication.isConnected.postValue(it)
            MarketApplication.isPreviouslyConnected.postValue(!it)
        })
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

    private fun setWindowFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}