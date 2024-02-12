package fr.eilco.flycompare.services.api_impl

import android.content.Context
import android.util.Log
import android.widget.Toast
import fr.eilco.flycompare.models.CountryResponse
import fr.eilco.flycompare.models.FlightListResponse
import fr.eilco.flycompare.models.FlightSearchData
import fr.eilco.flycompare.models.RequestFlightBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class FlightAPI {
    private val apiService = APIService().setup()
    fun getCountries(
        context: Context,
        onResponseSuccess: (response: Response<CountryResponse>) -> Unit,
        onFailure: () -> Unit
    ) {
        apiService.getCountries().enqueue(object : Callback<CountryResponse> {
            override fun onResponse(
                call: Call<CountryResponse>,
                response: Response<CountryResponse>
            ) {
                if (response.isSuccessful) {
                    onResponseSuccess(response)
                } else {
                    onFailure()
                    Toast.makeText(context, "Erreur code: ${response.code()}", Toast.LENGTH_SHORT)
                }
            }


            override fun onFailure(call: Call<CountryResponse>, t: Throwable) {
                if (t is SocketTimeoutException) {
                    onFailure()
                    Toast.makeText(context, "Temps d'attente trop long.", Toast.LENGTH_SHORT)
                } else {
                    onFailure()
                    Toast.makeText(context, "Erreur: ${t.message}", Toast.LENGTH_SHORT)
                }
            }
        })
    }

    fun getFlights(
        context: Context,
        data: RequestFlightBody,
        onResponseSuccess: (response: Response<FlightListResponse>) -> Unit,
        onFailure: () -> Unit
    ) {
        apiService.getFlights(body = data).enqueue(object : Callback<FlightListResponse> {
            override fun onResponse(
                call: Call<FlightListResponse>,
                response: Response<FlightListResponse>
            ) {
                if (response.isSuccessful) {
                    onResponseSuccess(response)
                } else {
                    onFailure()
                    Log.e("Erreur Mes vols 1", response.toString())
                    Toast.makeText(context, "Erreur code: ${response.code()}", Toast.LENGTH_SHORT)
                }
            }


            override fun onFailure(call: Call<FlightListResponse>, t: Throwable) {
                if (t is SocketTimeoutException) {
                    onFailure()

                    Log.e("Erreur Mes vols 2", "attente")
                    Toast.makeText(context, "Temps d'attente trop long.", Toast.LENGTH_SHORT)
                } else {
                    onFailure()

                    Log.e("Erreur Mes vols 3", t.message.toString())
                    Toast.makeText(context, "Erreur: ${t.message}", Toast.LENGTH_SHORT)
                }
            }
        })
    }

}