package fr.eilco.flycompare.services.api_impl

import android.content.Context
import android.widget.Toast
import fr.eilco.flycompare.models.CountryResponse
import fr.eilco.flycompare.models.GoodDealResponse
import fr.eilco.flycompare.utils.Keys
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class MapBoxAPI {
    private val apiService = APIService().setupMapBox()
    fun getSuggestions(context: Context, onResponseSuccess: (response: Response<GoodDealResponse>) -> Unit, onFailure: () -> Unit) {
        apiService.getSuggestions("calais", Keys.MAPBOX_ACCESS_TOKEN, Keys.MAPBOX_SESSION_TOKEN).enqueue(object : Callback<GoodDealResponse> {
            override fun onResponse(call: Call<GoodDealResponse>, response: Response<GoodDealResponse>) {
                if (response.isSuccessful) {
                    onResponseSuccess(response)
                } else {
                    Toast.makeText(context, "Erreur code: ${response.code()}", Toast.LENGTH_LONG)
                    onFailure()
                }
            }


            override fun onFailure(call: Call<GoodDealResponse>, t: Throwable) {
                if (t is SocketTimeoutException) {
                    onFailure()
                    Toast.makeText(context, "Temps d'attente trop long.", Toast.LENGTH_LONG)
                } else {
                    onFailure()
                    Toast.makeText(context, "Erreur: ${t.message}", Toast.LENGTH_LONG)
                }
            }
        })
    }
}