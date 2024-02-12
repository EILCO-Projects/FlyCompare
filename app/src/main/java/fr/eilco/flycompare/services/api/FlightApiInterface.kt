package fr.eilco.flycompare.services.api
import fr.eilco.flycompare.models.CountryResponse
import fr.eilco.flycompare.models.FlightListResponse
import fr.eilco.flycompare.models.RequestFlightBody
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FlightApiInterface {
    @GET("geo/hierarchy/flights/fr-FR")
    fun getCountries(): Call<CountryResponse>

    @POST("flights/live/search/create")
    fun getFlights(@Body body: RequestFlightBody): Call<FlightListResponse>
}