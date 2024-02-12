package fr.eilco.flycompare.models

data class CountryResponse (
    val status: String,
    val places: Map<String, Place>
)

data class  Place(
    val entityId: String,
    val name: String,
    val iata: String,
)