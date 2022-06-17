package andlima.group3.secondhand.repository

import andlima.group3.secondhand.api.auth.login.LoginApi
import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.model.login.LoginRequest
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: LoginApi) {
//    fun requestLogin(request: LoginRequest) = api.requestLogin(request)
    fun requestLogin(
        request: LoginRequest,
        action: (response: GetLoginResponse, code: Int, message: String) -> Unit
    ) {
        api.requestLogin(request).enqueue(object : retrofit2.Callback<GetLoginResponse>{
            override fun onResponse(
                call: Call<GetLoginResponse>,
                response: Response<GetLoginResponse>
            ) {
                call.timeout()
                if (response.isSuccessful) {
                    action(response.body()!!, response.code(), response.message())
                } else {
                    action(GetLoginResponse("", "", ""), response.code(), response.message())
                }
//                response.raw().close()
            }

            override fun onFailure(call: Call<GetLoginResponse>, t: Throwable) {
                action(GetLoginResponse("", "", ""), 0, t.message!!)
            }
        })
    }
}