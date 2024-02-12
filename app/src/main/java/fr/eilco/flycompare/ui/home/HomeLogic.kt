package fr.eilco.flycompare.ui.home

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import fr.eilco.flycompare.models.FlightSearchData
import fr.eilco.flycompare.ui.home.modals.FlightClassPopup
import fr.eilco.flycompare.utils.Helpers
import fr.eilco.flycompare.utils.KEnum
import java.time.LocalDate
import java.util.Calendar

class HomeLogic {
    fun chooseFlightClass(
        dialogLayout: View,
        context: Context,
        searchData: FlightSearchData,
        flight_class: TextView?,
        flight_nb_pers: TextView?
    ) {
        // Create the dialog instance
        val dialog = FlightClassPopup(context, dialogLayout, object : OnClassSelected {
            override fun selectClass(class_name: KEnum.FlightClassTypeEnum) {
                //Toast.makeText(requireContext(), "Item clicked: $class_name", Toast.LENGTH_SHORT).show()
                searchData.flight_class = class_name
                flight_class?.text = Helpers.getFlightClass(class_name)
            }

            override fun nbPers(value: Int) {
                searchData.nbPers = value
                flight_nb_pers?.text = value.toString()
            }
        })

        // Remove the parent from dialogLayout if it has one
        val parent = dialogLayout.parent as? ViewGroup
        parent?.removeView(dialogLayout)

        // Show the dialog
        dialog.show()
    }

    fun chooseFlightDate(
        context: Context,
        flight_date_departure: TextView?,
        flight_date_arrival: TextView?,
        searchData: FlightSearchData
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                run {
                    flight_date_departure?.text =
                        selectedDay.toString() + " " + Helpers.capitalize(
                            Helpers.formatDateFromCalendar(selectedMonth)
                        )
                    flight_date_arrival?.text = flight_date_departure?.text
                    searchData.departure = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                }
                if (searchData.flight_type == KEnum.FlightTypeEnum.Round_Trip) {
                    selectEndDate(context, selectedYear, selectedMonth, selectedDay, flight_date_arrival, searchData)
                }
            }, year, month, day
        )
        datePickerDialog.setMessage("Date de départ")
        datePickerDialog.show()
    }

    private fun selectEndDate(
        context: Context,
        year: Int,
        month: Int,
        day: Int,
        flight_date_arrival: TextView?,
        searchData: FlightSearchData
    ) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val arrivalDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                val departureDate = LocalDate.of(year, month, day)
                if (arrivalDate < departureDate) {
                    Toast.makeText(
                        context,
                        "La date de retour doit être supérieure à celle de départ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    selectEndDate(context, year, month, day, flight_date_arrival, searchData)
                } else {
                    run {
                        flight_date_arrival?.text =
                            selectedDay.toString() + " " + Helpers.capitalize(
                                Helpers.formatDateFromCalendar(selectedMonth)
                            )

                        searchData.arrival = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    }
                }
            }, year, month, day
        )

        datePickerDialog.setMessage("Date d'arrivée")
        datePickerDialog.show()
    }
}