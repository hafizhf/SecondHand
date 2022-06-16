package andlima.group3.secondhand.api.auth.login

import andlima.group3.secondhand.model.login.GetLoginResponse
import andlima.group3.secondhand.model.login.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    // Request login
    @POST("auth/login")
    fun requestLogin(@Body request: LoginRequest): Call<GetLoginResponse>
}