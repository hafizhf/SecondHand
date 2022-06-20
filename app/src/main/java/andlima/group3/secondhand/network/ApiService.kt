package andlima.group3.secondhand.network

import andlima.group3.secondhand.model.kategori.KategoriResponseItem
import andlima.group3.secondhand.model.register.RegisterResponse
import android.media.Image
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    @FormUrlEncoded
    fun registerUser(
        @Field("full_name") fullName : String,
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("phone_number") phoneNumber : Int,
        @Field("address") address :String,
        @Field("city") city :String
    ) : Call<RegisterResponse>

    @GET("seller/category")
    fun getCategory() : Call<List<KategoriResponseItem>>
}