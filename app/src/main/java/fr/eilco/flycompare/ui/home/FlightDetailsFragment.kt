package fr.eilco.flycompare.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.eilco.flycompare.R
import fr.eilco.flycompare.adapters.FlyAdapter
import fr.eilco.flycompare.adapters.MyFlights
import fr.eilco.flycompare.models.FlightSearchData
import fr.eilco.flycompare.models.MyDate
import fr.eilco.flycompare.models.PlaceId
import fr.eilco.flycompare.models.Query
import fr.eilco.flycompare.models.QueryLeg
import fr.eilco.flycompare.models.RequestFlightBody
import fr.eilco.flycompare.services.api_impl.FlightAPI
import fr.eilco.flycompare.utils.KEnum
import fr.eilco.flycompare.viewmodels.MyViewModel

class FlightDetailsFragment(flight: MyFlights?): Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.flight_screen_details, container, false)

        return view
    }
}