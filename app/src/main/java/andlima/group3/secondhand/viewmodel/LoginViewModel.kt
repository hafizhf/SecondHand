package andlima.group3.secondhand.viewmodel

import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.model.login.LoginRequest
import andlima.group3.secondhand.repository.AuthRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(api: AuthRepository): ViewModel() {

    private val apiHelper = api

    fun requestLogin(
        email: String,
        password: String,
        action: (response: GetLoginResponse, code: Int, message: String) -> Unit
    ) = apiHelper.requestLogin(LoginRequest(email, password), action)

//    fun requestLogin(
//        email: String,
//        password: String,
//        action: (response: GetLoginResponse, code: Int, message: String) -> Unit
//    ) {
//        apiHelper.requestLogin(LoginRequest(email, password))
//            .enqueue(object : retrofit2.Callback<GetLoginResponse>{
//                override fun onResponse(
//                    call: Call<GetLoginResponse>,
//                    response: Response<GetLoginResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        action(response.body()!!, response.code(), response.message())
//                    } else {
//                        action(GetLoginResponse("", "", ""), response.code(), response.message())
//                    }
//                }
//
//                override fun onFailure(call: Call<GetLoginResponse>, t: Throwable) {
//                    action(GetLoginResponse("", "", ""), 0, t.message!!)
//                }
//            })
//    }
}