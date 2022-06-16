package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.model.login.LoginRequest
import andlima.group3.secondhand.repository.AuthRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(api: AuthRepository): ViewModel() {

//    @Inject
//    lateinit var api: AuthRepository

    val apiHelper = api

    var liveDataLogin: MutableLiveData<GetLoginResponse> = MutableLiveData()

    suspend fun requestLogin(
        email: String,
        password: String,
        action: (response: GetLoginResponse, code: Int, message: String) -> Unit
    ) {
        apiHelper.requestLogin(LoginRequest(email, password))
            .enqueue(object : retrofit2.Callback<GetLoginResponse>{
                override fun onResponse(
                    call: Call<GetLoginResponse>,
                    response: Response<GetLoginResponse>
                ) {
                    if (response.isSuccessful) {
                        action(response.body()!!, response.code(), response.message())
                    } else {
                        action(GetLoginResponse("", "", ""), response.code(), response.message())
                    }
                }

                override fun onFailure(call: Call<GetLoginResponse>, t: Throwable) {
                    action(GetLoginResponse("", "", ""), 0, t.message!!)
                }
            })
    }
}