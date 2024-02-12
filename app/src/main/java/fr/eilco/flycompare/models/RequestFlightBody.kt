package fr.eilco.flycompare.models

data class RequestFlightBody(
    val query: Query
)

data class Query(
    val market: String = "FR",
    val locale: String = "fr-FR",
    val currency: String = "EUR",
    val queryLegs: List<QueryLeg>,
    val cabinClass: String?,
    val adults: Int = 1,
    val includeBaggageData: Boolean = true
)

data class QueryLeg(
    val originPlaceId: PlaceId,
    val destinationPlaceId: PlaceId,
    val date: MyDate

)

data class PlaceId(
    val entityId: String?
)

data class MyDate(
    var year: Int,
    var month: Int,
    var day: Int
)