package fr.eilco.flycompare

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import fr.eilco.flycompare.models.FlightSearchData
import fr.eilco.flycompare.ui.home.FlightFragment
import fr.eilco.flycompare.viewmodels.MyViewModel


@Suppress("DEPRECATION")
class FlightActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flights_screen_activity)

        val searchData = intent.getParcelableExtra<FlightSearchData>("searchData")
        val viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flights_content, FlightFragment(searchData!!, viewModel))
        transaction.addToBackStack(null)
        transaction.commit()
    }

}