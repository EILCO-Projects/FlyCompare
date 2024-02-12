package fr.eilco.flycompare.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.eilco.flycompare.FlightActivity
import java.time.format.DateTimeFormatter
import java.util.Locale
import fr.eilco.flycompare.R
import fr.eilco.flycompare.adapters.HomeAdapter
import fr.eilco.flycompare.models.FlightSearchData
import fr.eilco.flycompare.models.Place
import fr.eilco.flycompare.models.Suggestion
import fr.eilco.flycompare.services.api_impl.FlightAPI
import fr.eilco.flycompare.services.api_impl.MapBoxAPI
import fr.eilco.flycompare.utils.Helpers
import fr.eilco.flycompare.utils.KEnum

class HomeFragment : Fragment() {

    private val _logic: HomeLogic = HomeLogic()
    private var searchData = FlightSearchData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.home_screen, container, false);
        val horizontalRecyclerView = view?.findViewById<RecyclerView>(R.id.home_good_deals_cards)

        val screenSize = getScreenSize(requireContext())
        Log.e("Screen Size", screenSize.toString())
        if(screenSize.first < 1400) {

        }

        /**
         * Récupération des éléments à utiliser
         */

        // Sélection des dates
        val flight_date_btn = view?.findViewById<LinearLayout>(R.id.flight_date_interval)
        val flight_date_departure = view?.findViewById<TextView>(R.id.flight_date_departure)
        val flight_date_arrival = view?.findViewById<TextView>(R.id.flight_date_arrival)
        val flight_date_arrival_layout =
            view?.findViewById<LinearLayout>(R.id.flight_date_arrival_layout)

        // Selection de la classe du vol
        val flight_class_btn = view?.findViewById<LinearLayout>(R.id.flight_class_btn)
        val flight_class_type = view?.findViewById<TextView>(R.id.flight_class_type)
        val flight_nb_pers = view?.findViewById<TextView>(R.id.flight_nb_pers)

        // Selection du type de vol (Aller simple/Aller retour)
        val flight_one_way = view?.findViewById<TextView>(R.id.one_way_btn)
        val flight_round_trip = view?.findViewById<TextView>(R.id.round_trip_btn)

        // Selection le lieu de départ et d'arrivée
        val flight_permute_departure_destination =
            view?.findViewById<ImageView>(R.id.flight_permute)
        val departureTextView =
            view?.findViewById<AutoCompleteTextView>(R.id.flight_departure)
        val arrivalTextView =
            view?.findViewById<AutoCompleteTextView>(R.id.flight_destination)

        /***
         * On récupère les villes pour la suggestion du formulaire
         */
        //val countryResponse = FlightAPI().getCountries(requireContext())

        val suggestions = ArrayList<Suggestion>()
        MapBoxAPI().getSuggestions(
            requireContext(),
            onResponseSuccess = { response ->
                run {
                    suggestions.addAll(response.body()!!.suggestions)
                    horizontalRecyclerView?.adapter = HomeAdapter(suggestions)
                }
            },
            onFailure = {
                horizontalRecyclerView?.adapter = HomeAdapter(suggestions)
            })


        FlightAPI().getCountries(
            requireContext(),
            onFailure = {

            },
            onResponseSuccess = { response ->
                run {
                    val places = ArrayList<String>()
                    val placesFull = ArrayList<Place>()

                    response.body()!!.places.entries.forEach { (_, value) ->
                        run {
                            places.add(value.name)
                            placesFull.add(value)
                        }
                    }
                    val homeLoader = view?.findViewById<ConstraintLayout>(R.id.home_screen_loader)
                    val homeContent = view?.findViewById<LinearLayout>(R.id.home_screen_content)

                    if (places.isEmpty()) {
                        homeLoader?.isVisible = true
                        homeContent?.isVisible = false
                    } else {
                        homeLoader?.isVisible = false
                        homeContent?.isVisible = true


                        val adapter = CustomAdapter(requireContext(), places)

                        departureTextView?.setAdapter(adapter)

                        departureTextView?.setOnItemClickListener { parent, view, position, id ->
                            val selectedItem = parent.getItemAtPosition(position).toString()

                            searchData.departure_town_entity_id = placesFull.get(places.indexOf(selectedItem)).entityId
                            searchData.departure_town = selectedItem
                            departureTextView?.setText(selectedItem)
                        }

                        arrivalTextView?.setAdapter(adapter)

                        arrivalTextView?.setOnItemClickListener { parent, view, position, id ->
                            val selectedItem = parent.getItemAtPosition(position).toString()

                            searchData.arrival_town_entity_id = placesFull.get(places.indexOf(selectedItem)).entityId
                            searchData.arrival_town = selectedItem
                            arrivalTextView?.setText(selectedItem)
                        }
                    }
                }
            })


        /***
         * Remplissage du formulaire de recherche
         */

        // Modification des valeurs des champs du formulaire
        val formatter = DateTimeFormatter.ofPattern("d MMM", Locale.FRENCH)
        flight_class_type?.text = Helpers.getFlightClass(searchData.flight_class)
        flight_nb_pers?.text = searchData.nbPers.toString()
        flight_date_departure?.text =
            Helpers.capitalize(searchData.departure.format(formatter))
        flight_date_arrival?.text =
            Helpers.capitalize(searchData.arrival.format(formatter))
        departureTextView?.hint =
            if (searchData.departure_town?.isEmpty() != true) searchData.departure_town else "Lieu de départ"
        arrivalTextView?.hint =
            if (searchData.arrival_town?.isEmpty() != true) searchData.arrival_town else "Lieu d'arrivée"

        flight_permute_departure_destination?.setOnClickListener {
            val temp = departureTextView?.text
            departureTextView?.text = arrivalTextView?.text;
            arrivalTextView?.text = temp;
        }

        /***
         * Switch
         */

        /***
         * Switch flight type
         */

        flight_one_way?.setOnClickListener {
            searchData.flight_type = KEnum.FlightTypeEnum.One_Way
            // Mettez à jour l'arrière-plan et forcez la mise à jour de l'interface utilisateur
            switchFlightType(
                flight_one_way,
                flight_round_trip,
                flight_date_arrival_layout,
                KEnum.FlightTypeEnum.One_Way
            )
        }

        flight_round_trip?.setOnClickListener {
            searchData.flight_type = KEnum.FlightTypeEnum.Round_Trip
            // Mettez à jour l'arrière-plan et forcez la mise à jour de l'interface utilisateur
            switchFlightType(
                flight_one_way,
                flight_round_trip,
                flight_date_arrival_layout,
                KEnum.FlightTypeEnum.Round_Trip
            )
        }

        if (searchData.flight_type == KEnum.FlightTypeEnum.Round_Trip) {
            switchFlightType(
                flight_one_way,
                flight_round_trip,
                flight_date_arrival_layout,
                KEnum.FlightTypeEnum.Round_Trip
            )
        } else {
            switchFlightType(
                flight_one_way,
                flight_round_trip,
                flight_date_arrival_layout,
                KEnum.FlightTypeEnum.One_Way
            )
        }

        /**
         * Choix des dates d'aller - retour
         */
        // Choix de la date d'aller et de retour de voyage
        flight_date_btn?.setOnClickListener {
            _logic.chooseFlightDate(
                requireContext(),
                flight_date_departure,
                flight_date_arrival,
                searchData
            )
        }

        //Choix de la classe du voyage
        val dialogLayout = inflater?.inflate(R.layout.select_flight_class, null)
        flight_class_btn?.setOnClickListener {
            _logic.chooseFlightClass(
                dialogLayout!!,
                requireContext(),
                searchData,
                flight_class_type,
                flight_nb_pers
            )
        }

        val go_to_next_page_btn = view?.findViewById<TextView>(R.id.home_search_button)
        go_to_next_page_btn?.setOnClickListener {
            submitForm(searchData.departure_town, searchData.arrival_town);
        }

        /***
         * Retourner la vue
         */
        return view;
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun switchFlightType(
        flight_one_way: TextView?,
        flight_round_trip: TextView?,
        flight_date_arrival: LinearLayout?,
        type: KEnum.FlightTypeEnum
    ) {
        if (type == KEnum.FlightTypeEnum.Round_Trip) {
            flight_one_way?.background = null
            flight_date_arrival?.isVisible = true
            flight_round_trip?.background = resources.getDrawable(R.drawable.btn_primary)
        } else {
            flight_date_arrival?.isVisible = false
            flight_round_trip?.background = null
            flight_one_way?.background = resources.getDrawable(R.drawable.btn_primary)
        }
    }


    private fun submitForm(from: String?, to: String?) {
        if (from?.isEmpty() == true) {
            Toast.makeText(requireContext(), "Renseigner le lieu de départ", Toast.LENGTH_LONG)
                .show()
        } else if (to?.isEmpty() == true) {
            Toast.makeText(requireContext(), "Renseigner le lieu d'arrivée", Toast.LENGTH_LONG)
                .show()
        } else {
            Log.e("vols search data", searchData.toString())
            val intent = Intent(requireContext(), FlightActivity::class.java)
            intent.putExtra("searchData", searchData)
            startActivity(intent)
        }
    }

    fun getScreenSize(context: Context): Pair<Int, Int> {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        return Pair(width, height)
    }
}

class CustomAdapter(context: Context, private val places: List<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, places) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false)

        val textView = rowView.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)

        return rowView
    }
}


interface OnClassSelected {
    fun selectClass(class_name: KEnum.FlightClassTypeEnum);
    fun nbPers(value: Int);
}