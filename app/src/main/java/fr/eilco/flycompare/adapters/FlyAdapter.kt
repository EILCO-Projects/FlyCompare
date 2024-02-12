package fr.eilco.flycompare.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fr.eilco.flycompare.FlightActivity
import fr.eilco.flycompare.FlightDetailsActivity
import fr.eilco.flycompare.R
import fr.eilco.flycompare.models.Carrier
import fr.eilco.flycompare.models.DateTime
import fr.eilco.flycompare.models.Item
import fr.eilco.flycompare.models.Results
import fr.eilco.flycompare.utils.Helpers
import fr.eilco.flycompare.utils.Keys
import fr.eilco.flycompare.viewmodels.MyViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FlyAdapter(private val result: Results?, private val parentContext: Context, private val saveFlight: (MyFlights) -> Unit) :
    RecyclerView.Adapter<FlyAdapter.ViewHolder>() {

    val flights = reformatData()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val airlineCompanyTextView = view.findViewById<TextView>(R.id.card_airline_company)
        val departureAirportTextView = view.findViewById<TextView>(R.id.card_departure_airport)
        val departureTimeTextView = view.findViewById<TextView>(R.id.card_departure_time)
        val departureCityTextView = view.findViewById<TextView>(R.id.card_departure_city)
        val arrivalAirportTextView = view.findViewById<TextView>(R.id.card_arrival_airport)
        val arrivalTimeTextView = view.findViewById<TextView>(R.id.card_arrival_time)
        val arrivalCityTextView = view.findViewById<TextView>(R.id.card_arrival_city)
        val flightDuration = view.findViewById<TextView>(R.id.card_flight_duration)
        val flightStops = view.findViewById<TextView>(R.id.card_escale_value)
        val price = view.findViewById<TextView>(R.id.price)

        val goToNextPage = view.findViewById<LinearLayout>(R.id.card_flight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.flights_card_item, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight = flights[position]
        holder.airlineCompanyTextView.text = flight.flyProps.first().agent
        holder.departureAirportTextView.text = flight.legs.first().stops.first().companyICAO
        holder.departureTimeTextView.text = formatTime(flight.legs.first().departureTime)
        holder.departureCityTextView.text = if(flight.legs.first().origin?.length!! > 12) flight.legs.first().origin?.substring(0, 12) + "..." else flight.legs.first().origin
        holder.arrivalAirportTextView.text = flight.legs.last().stops.last().companyICAO
        holder.arrivalTimeTextView.text =formatTime(flight.legs.first().arrivalTime)
        holder.arrivalCityTextView.text = if(flight.legs.first().destination?.length!! > 12) flight.legs.first().destination?.substring(0, 12) + "..." else flight.legs.first().destination
        holder.flightDuration.text = Helpers.convertMinutesToString(flight.legs.first().durationInMinutes)
        holder.flightStops.text = if(flight.legs.first().stops.isEmpty()) "Vol direct"  else flight.legs.first().stops.size.toString() + "Escale(s)"
        holder.price.text = flight.flyProps.first().amount.toString()

        holder.goToNextPage.setOnClickListener{
            if(position % 2 == 0){
                val intent = Intent(parentContext, FlightDetailsActivity::class.java)
                saveFlight(flight)
                ContextCompat.startActivity(parentContext, intent, null)
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(flight.flyProps.first().lien))
                ContextCompat.startActivity(parentContext, intent, null)
            }
        }
    }

    override fun getItemCount(): Int = flights.size

    fun reformatData(): List<MyFlights> {
        var flights = ArrayList<MyFlights>();

        result?.itineraries?.forEach { (_, value) ->
            run {
                val legs = retrieveLegs(value.legIds)
                val options = retrieveOptions(value.pricingOptions.first().items)
                var duration = 0
                legs.forEach { leg ->
                    run {
                        duration += leg.durationInMinutes
                    }
                }
                var nbEscale = legs.first().stopCount

                flights.add(
                    MyFlights(
                        legs,
                        Helpers.convertMinutesToString(duration),
                        options,
                        nbEscale
                    )
                )
            }
        }

        return flights
    }

    private fun formatTime(dateTime: LocalDateTime): String? {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return dateTime.format(formatter)
    }

    fun retrieveLegs(ids: List<String>): List<FlightLeg> {
        var legs = ArrayList<FlightLeg>()

        ids.forEach { id ->
            run {
                result?.legs?.forEach { (key, value) ->
                    run {
                        if (id == key) {
                            var origin = getPlace(value.originPlaceId)
                            var destination = getPlace(value.destinationPlaceId)
                            val departureTime = getTime(value.departureDateTime)
                            val arrivalTime = getTime(value.arrivalDateTime)
                            val segments = retrieveSegments(value.segmentIds)
                            legs?.add(
                                FlightLeg(
                                    origin,
                                    destination,
                                    departureTime,
                                    arrivalTime,
                                    segments,
                                    value.durationInMinutes,
                                    value.stopCount
                                )
                            )
                        }
                    }
                }
            }
        }

        return legs
    }

    private fun retrieveSegments(ids: List<String>): List<LegSegment> {
        var segments = ArrayList<LegSegment>()

        ids.forEach { id ->
            run {
                result?.segments?.entries?.forEach { (key, value) ->
                    run {
                        val departureTime = value.departureDateTime
                        val arrivalTime = value.arrivalDateTime
                        val origin = getPlace(value.originPlaceId)
                        val destination = getPlace(value.destinationPlaceId)
                        val company = getCompany(value.operatingCarrierId)
                        segments.add(
                            LegSegment(
                                origin = origin,
                                destination = destination,
                                departureTime = LocalDateTime.of(
                                    departureTime.year,
                                    departureTime.month,
                                    departureTime.day,
                                    departureTime.hour,
                                    departureTime.minute,
                                    departureTime.second
                                ),
                                arrivalTime = LocalDateTime.of(
                                    arrivalTime.year,
                                    arrivalTime.month,
                                    arrivalTime.day,
                                    arrivalTime.hour,
                                    arrivalTime.minute,
                                    arrivalTime.second
                                ),
                                durationInMinutes = value.durationInMinutes,
                                companyICAO = company?.icao ?: "CAL",
                                companyName = company?.name ?: "NONAME",
                            )
                        )
                    }
                }
            }
        }

        return segments

    }

    private fun getCompany(id: String): Carrier? {
        result?.carriers?.entries?.forEach { (key, value) ->
            run {
                if(id == key) {
                    return value
                }
            }
        }
        return null
    }

    private fun getTime(time: DateTime): LocalDateTime {
        return LocalDateTime.of(
            time.year,
            time.month,
            time.day,
            time.hour,
            time.minute,
            time.second
        )
    }

    fun getPlace(placeId: String): String {
        result?.places?.entries?.forEach { (key, value) ->
            run {
                return value.name
            }
        }

        return "NONAME"
    }

    fun retrieveOptions(ids: List<Item>): List<FlyProps> {
        var props = ArrayList<FlyProps>()

        ids.forEach { id ->
            run {
                val amount = id.price.amount.toDouble() / 1000
                val lien = id.deepLink
                var agent = ""
                result?.agents?.forEach { (key, value) ->
                    run {
                        if (id.agentId == key) {
                            agent = value.name
                        }
                    }
                }

                props.add(FlyProps(amount, agent, lien ?: Keys.DEFAULT_LINK))
            }
        }

        return props
    }
}

data class MyFlights(
    val legs: List<FlightLeg>,
    val duration: String,
    val flyProps: List<FlyProps>,
    val nbEscales: Int
)

data class FlightLeg(
    val origin: String,
    val destination: String,
    val departureTime: LocalDateTime,
    val arrivalTime: LocalDateTime,
    val stops: List<LegSegment>,
    val durationInMinutes: Int,
    val stopCount: Int
)

data class LegSegment(
    val origin: String,
    val destination: String,
    val departureTime: LocalDateTime,
    val arrivalTime: LocalDateTime,
    val durationInMinutes: Int,
    val companyName: String,
    val companyICAO: String,
)

data class FlyProps(
    val amount: Double,
    val agent: String,
    val lien: String,
)