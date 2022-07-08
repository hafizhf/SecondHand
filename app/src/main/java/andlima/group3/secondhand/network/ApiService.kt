package andlima.group3.secondhand.network

import andlima.group3.secondhand.model.daftarjual.SellerProductsItem
import andlima.group3.secondhand.model.daftarjual.diminati.SellerOrdersItem
import andlima.group3.secondhand.model.daftarjual.terimatolak.PatchOrderResponse
import andlima.group3.secondhand.model.daftarjual.terimatolak.StatusTawaran
import andlima.group3.secondhand.model.jual.PostProductResponse
import andlima.group3.secondhand.model.kategori.KategoriResponseItem
import andlima.group3.secondhand.model.notification.NotificationResponseItem
import andlima.group3.secondhand.model.produk.ProductResponse
import andlima.group3.secondhand.model.register.RegisterResponse
import andlima.group3.secondhand.model.user.UpdateProfileResponse
import andlima.group3.secondhand.model.user.UserDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
//--------------------------------------------------------------------------------------------------------
    @Multipart
    @PUT("auth/user")
    fun profileUser(
    @Header("access_token") accessToken: String,
    @Part("full_name") fullName :RequestBody ,
    @Part("phone_number") phoneNumber : RequestBody,
    @Part("address") address :RequestBody,
    @Part("city") city :RequestBody,
    @Part image : MultipartBody.Part
    ) : Call<UpdateProfileResponse>


  //------------------------------------------------------------------------------------------------------






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
    @GET("seller/product/{id}")
    fun getDetailProduct(
        @Header("access_token") accessToken: String,
        @Path("id") id : Int
    ) : Call<ProductResponse>

    @GET("seller/order")
    fun getSellerAllOrder(
        @Header("access_token") accessToken: String
    ) : Call<List<SellerOrdersItem>>

    @Multipart
    @POST("seller/product")
    fun postProduct(
        @Header("access_token") accessToken: String,
        @Part image : MultipartBody.Part,
        @Part("name") name : RequestBody,
        @Part("description") description : RequestBody,
        @Part("base_price") basePrice : RequestBody,
        @Part("category_ids") categoryIDs : RequestBody,
        @Part("location") location : RequestBody
    ) : Call<PostProductResponse>

    @GET("notification")
    fun getNotif(
        @Header("access_token") accessToken: String
        ) : Call<List<NotificationResponseItem>>

    @GET("seller/order/{id}")
    fun getDetailOrder(
        @Header("access_token") accessToken: String,
        @Path("id") id : Int
    ) : Call<SellerOrdersItem>

    @PATCH("seller/order/{id}")
    fun prosesOrder(
        @Header("access_token") accessToken: String,
        @Path("id") id : Int,
        @Body status : StatusTawaran
    ) : Call<PatchOrderResponse>
    @PATCH("notification/{id}")
    fun readNotif(
        @Header("access_token") accessToken: String,
        @Path("id") id : Int,
    ) : Call<NotificationResponseItem>
}