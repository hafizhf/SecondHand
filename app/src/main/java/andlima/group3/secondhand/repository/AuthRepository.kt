package andlima.group3.secondhand.repository

import andlima.group3.secondhand.api.auth.login.LoginApi
import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.model.login.LoginRequest
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: LoginApi) {
    suspend fun requestLogin(request: LoginRequest) = api.requestLogin(request)

    lateinit var a : GetLoginResponse

//    fun requestLogin(
//        email: String,
//        password: String,
//        action: (response: GetLoginResponse, code: Int, message: String) -> Unit
//        ) {
//        api.requestLogin(LoginRequest(email, password))
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