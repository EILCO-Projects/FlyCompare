package fr.eilco.flycompare.ui.home.modals

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.content.ContextCompat
import fr.eilco.flycompare.R
import fr.eilco.flycompare.ui.home.OnClassSelected
import fr.eilco.flycompare.utils.KEnum

class FlightClassPopup(
    private val context: Context,
    private val resource: View,
    private val onClassSelected: OnClassSelected
) : Dialog(context) {

    val class_eco = resource?.findViewById<Button>(R.id.flight_class_eco)
    val class_h_eco = resource?.findViewById<Button>(R.id.flight_class_h_eco)
    val class_business = resource?.findViewById<Button>(R.id.flight_class_business)
    val class_premium = resource?.findViewById<Button>(R.id.flight_class_premium)
    val nbPers = resource?.findViewById<AutoCompleteTextView>(R.id.edit_nb_pers)
    val validerBtn = resource?.findViewById<Button>(R.id.popup_close_btn)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        resource.minimumWidth = 500
        resource.minimumHeight = 350

        var type = KEnum.FlightClassTypeEnum.CABIN_CLASS_ECONOMY

        class_eco?.setOnClickListener {
            type = KEnum.FlightClassTypeEnum.CABIN_CLASS_ECONOMY
            selectFlightType(type)
        }

        class_h_eco?.setOnClickListener {
            type = KEnum.FlightClassTypeEnum.CABIN_CLASS_PREMIUM_ECONOMY
            selectFlightType(type)
        }

        class_business?.setOnClickListener {
            type = KEnum.FlightClassTypeEnum.CABIN_CLASS_BUSINESS
            selectFlightType(type)
        }

        class_premium?.setOnClickListener {
            type = KEnum.FlightClassTypeEnum.CABIN_CLASS_FIRST
            selectFlightType(type)
        }

        validerBtn?.setOnClickListener {
            onClassSelected.selectClass(type)
            onClassSelected.nbPers(nbPers?.text.toString().toIntOrNull() ?: 1)
            dismiss()
        }

        setContentView(resource)

    }

    private fun selectFlightType(type: KEnum.FlightClassTypeEnum) {
        deactivateAllBtn()
        when (type) {
            KEnum.FlightClassTypeEnum.CABIN_CLASS_FIRST -> {
                class_premium?.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
            KEnum.FlightClassTypeEnum.CABIN_CLASS_PREMIUM_ECONOMY -> {
                class_h_eco?.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
            KEnum.FlightClassTypeEnum.CABIN_CLASS_BUSINESS -> {
                class_business?.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
            KEnum.FlightClassTypeEnum.CABIN_CLASS_ECONOMY -> {
                class_eco?.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
        }
    }

    private fun deactivateAllBtn() {
        class_eco?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        class_h_eco?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        class_business?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        class_premium?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    }
}