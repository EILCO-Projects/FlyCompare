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
import fr.eilco.flycompare.models.FlightSearchData
import fr.eilco.flycompare.R
import fr.eilco.flycompare.adapters.FlyAdapter
import fr.eilco.flycompare.adapters.FlyAgentAdapter
import fr.eilco.flycompare.models.MyDate
import fr.eilco.flycompare.models.PlaceId
import fr.eilco.flycompare.models.Query
import fr.eilco.flycompare.models.QueryLeg
import fr.eilco.flycompare.models.RequestFlightBody
import fr.eilco.flycompare.services.api_impl.FlightAPI
import fr.eilco.flycompare.utils.KEnum
import fr.eilco.flycompare.viewmodels.MyViewModel

class FlightFragment(private val searchData: FlightSearchData, private val viewModel: MyViewModel) :
    Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.flights_cards, container, false)

        val title = view?.findViewById<TextView>(R.id.splash_flights_sreen_subtitle)
        title?.text = searchData?.departure_town + " - " + searchData?.arrival_town

        val state = view?.findViewById<TextView>(R.id.flights_text_state)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_of_flights)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val flightLoader = view?.findViewById<ProgressBar>(R.id.flights_spinner)
        flightLoader?.isVisible = true
        state?.isVisible = false

        val body = formatRequest(searchData)
        Log.e("Mes vols body", body.toString())

        FlightAPI().getFlights(
            requireContext(),
            body,
            onResponseSuccess = { response ->
                run {
                    flightLoader?.isVisible = false
                    Log.e("Mes vols", response.body().toString())
                    if (response.body()?.content?.results?.itineraries?.isEmpty() == true) {
                        if (response.body()?.content?.results?.agents?.isNotEmpty() == true) {
                            recyclerView.adapter = FlyAgentAdapter(
                                response.body()?.content?.results,
                                requireActivity(),
                                requireContext()
                            )
                        } else {
                            state?.isVisible = true
                            state?.text = "Aucun itinéraire pour ce trajet à cette date"
                        }
                    } else {
                        recyclerView.adapter = FlyAdapter(
                            response.body()?.content?.results,
                            requireContext()
                        ) { flight ->
                            run {
                                viewModel.setMyFlights(flight)
                            }
                        }
                    }
                }
            },
            onFailure = {
                flightLoader?.isVisible = false
                state?.isVisible = true
                state?.text = "Erreur API : Too Many requests"
            })


        return view
    }

    fun formatRequest(searchData: FlightSearchData): RequestFlightBody {
        var queries = ArrayList<QueryLeg>()
        queries.add(
            QueryLeg(
                originPlaceId = PlaceId(
                    entityId = searchData.departure_town_entity_id
                ),
                destinationPlaceId = PlaceId(
                    entityId = searchData.arrival_town_entity_id
                ),
                date = MyDate(
                    year = searchData.departure.year,
                    month = searchData.departure.monthValue,
                    day = searchData.departure.dayOfMonth
                )
            )
        )
        if (searchData.flight_type == KEnum.FlightTypeEnum.Round_Trip) {
            queries.add(
                QueryLeg(
                    originPlaceId = PlaceId(
                        entityId = searchData.departure_town_entity_id
                    ),
                    destinationPlaceId = PlaceId(
                        entityId = searchData.arrival_town_entity_id
                    ),
                    date = MyDate(
                        year = searchData.arrival.year,
                        month = searchData.arrival.monthValue,
                        day = searchData.arrival.dayOfMonth
                    )
                )
            )
        }


        val bodyQuery = Query(
            adults = searchData.nbPers,
            queryLegs = queries,
            cabinClass = searchData.flight_class.toString(),
        )

        return RequestFlightBody(bodyQuery)
    }
}