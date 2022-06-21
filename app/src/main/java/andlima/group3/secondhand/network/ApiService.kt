package andlima.group3.secondhand.network

import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import andlima.group3.secondhand.model.jual.PostProductResponse
import andlima.group3.secondhand.model.kategori.KategoriResponseItem
import andlima.group3.secondhand.model.register.RegisterResponse
import andlima.group3.secondhand.model.user.UserDetailResponse
import android.media.Image
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

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

    @GET("auth/user")
    fun getDetailUser(
        @Header("access_token") accessToken : String
    ) : Call<UserDetailResponse>

    @GET("seller/product")
    fun getSellerAllProduct(
        @Header("access_token") accessToken: String
    ) : Call<List<SellerProductsItem>>

    @Multipart
    @POST("seller/product")
    fun postProduct(
        @Header("access__token") accessToken: String,
        @Part("name") name : String,
        @Part("description") description : String,
        @Part("base_price") basePrice : Int,
        @Part("category_ids") categoryIDs : List<Int>,
        @Part("location") location : String,
        @Part image : MultipartBody.Part
    ) : Call<PostProductResponse>
}