package andlima.group3.secondhand.DI

import andlima.group3.secondhand.network.ApiService
import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    const val BASE_URL = "https://market-final-project.herokuapp.com/"

    private  val logging : HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    private  val clint = OkHttpClient.Builder().addInterceptor(logging).build()

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clint)
            .build()

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit) : ApiService =
        retrofit.create(ApiService::class.java)

//    @Provides
//    @Singleton
//    fun provideDB(context: Application) : FavoriteDatabase{
//        return  FavoriteDatabase.getInstance(context)!!
//    }
//
//    @Provides
//    @Singleton
//    fun provideDao(favDB : FavoriteDatabase) : FavoriteDao{
//        return  favDB.favoriteDao()
//    }


}