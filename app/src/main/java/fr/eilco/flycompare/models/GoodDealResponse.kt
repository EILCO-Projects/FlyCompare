package fr.eilco.flycompare.models


data class GoodDealResponse (
    val suggestions: List<Suggestion>
)

data class Suggestion (
    val name: String,
    val place_formatted: String,
    val context: SuggestionContext?,
)

data class SuggestionContext (
    val country: Country?,
    val region: Region?
)

data class Country(
    val name: String
)

data class Region (
    val name: String
)
