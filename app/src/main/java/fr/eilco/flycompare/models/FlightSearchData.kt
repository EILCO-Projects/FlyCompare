package fr.eilco.flycompare.models

import android.os.Parcelable
import fr.eilco.flycompare.utils.KEnum
import java.time.LocalDate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightSearchData (
    var departure : LocalDate = LocalDate.now(),
    var arrival : LocalDate =  LocalDate.now().plusDays(1),
    var flight_class: KEnum.FlightClassTypeEnum = KEnum.FlightClassTypeEnum.CABIN_CLASS_ECONOMY,
    var flight_type: KEnum.FlightTypeEnum = KEnum.FlightTypeEnum.One_Way,
    var departure_town: String? = "",
    var arrival_town: String? = "",
    var arrival_town_iata: String? = "",
    var arrival_town_entity_id: String? = "",
    var departure_town_entity_id: String? = "",
    var departure_town_iata: String? = "",
    var nbPers: Int = 1
) : Parcelable