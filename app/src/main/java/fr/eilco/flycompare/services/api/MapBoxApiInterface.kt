package fr.eilco.flycompare.services.api

import fr.eilco.flycompare.models.CountryResponse
import fr.eilco.flycompare.models.GoodDealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MapBoxApiInterface {
    @GET("suggest")
    fun getSuggestions(@Query("q") query: String, @Query("access_token") accessToken: String,@Query("session_token") sessionToken: String): Call<GoodDealResponse>
}