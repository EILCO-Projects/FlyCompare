package fr.eilco.flycompare

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import fr.eilco.flycompare.adapters.MyFlights
import fr.eilco.flycompare.models.FlightSearchData
import fr.eilco.flycompare.ui.home.FlightDetailsFragment
import fr.eilco.flycompare.ui.home.FlightFragment
import fr.eilco.flycompare.utils.Helpers
import fr.eilco.flycompare.viewmodels.MyViewModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Suppress("DEPRECATION")
class FlightDetailsActivity: FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_screen_details_activity)


        val viewModel = ViewModelProvider(this)[MyViewModel::class.java]


        //var flight = null
        viewModel.getMyFlights().observe(this) { fl ->
            run {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.flights_details_screen, FlightDetailsFragment(fl))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flights_details_screen, FlightDetailsFragment(null))
        transaction.addToBackStack(null)
        transaction.commit()
    }
}