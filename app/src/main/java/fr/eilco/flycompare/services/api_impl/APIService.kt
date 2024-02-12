package fr.eilco.flycompare.services.api_impl

import fr.eilco.flycompare.services.api.FlightApiInterface
import fr.eilco.flycompare.services.api.MapBoxApiInterface
import fr.eilco.flycompare.utils.Keys
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIService {
    fun setup(): FlightApiInterface {
        val interceptor = ApiKeyInterceptor(Keys.API_KEY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor) .build()

        val service: FlightApiInterface by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(Keys.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(FlightApiInterface::class.java)
        }

        return service
    }
    fun setupMapBox(): MapBoxApiInterface {
        val service: MapBoxApiInterface by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(Keys.MAPBOX_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(MapBoxApiInterface::class.java)
        }

        return service
    }
}


class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-api-key", apiKey)
            .build()
        return chain.proceed(request)
    }
}