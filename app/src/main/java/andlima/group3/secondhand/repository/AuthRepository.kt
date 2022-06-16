package andlima.group3.secondhand.repository

import andlima.group3.secondhand.api.auth.login.LoginApi
import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.model.login.LoginRequest
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: LoginApi) {
    fun requestLogin(request: LoginRequest) = api.requestLogin(request)
}