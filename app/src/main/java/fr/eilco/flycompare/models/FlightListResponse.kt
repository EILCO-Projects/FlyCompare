package fr.eilco.flycompare.models

data class FlightListResponse (
    val sessionToken: String,
    val status: String,
    val action: String,
    val content: Content
)

data class Content(
    val results: Results
)

data class Results(
    val itineraries: Map<String, Itinerary>,
    val legs: Map<String, Leg>,
    val carriers: Map<String, Carrier>,
    val agents: Map<String, Agent>,
    val segments: Map<String, FlightSegment>,
    val places: Map<String, FlightPlace>,
    val alliances: Map<String, Alliance>
)

data class FlightSegment (
    val originPlaceId: String,
    val destinationPlaceId: String,
    val departureDateTime: DateTime,
    val arrivalDateTime: DateTime,
    val durationInMinutes: Int,
    val operatingCarrierId: String,
)

data class FlightPlace (
    val entityId: String,
    val name: String,
    val iata: String,
    val parentId: String,
)

data class Itinerary(
    val pricingOptions: List<PricingOption>,
    val legIds: List<String>, // Remplacez le type Any par le type approprié si possible
)

data class PricingOption(
    val price: Price,
    val items: List<Item>
)

data class Price(
    val amount: String,
)

data class Item(
    val price: Price,
    val agentId: String,
    val deepLink: String?
)

data class Leg(
    val originPlaceId: String,
    val destinationPlaceId: String,
    val departureDateTime: DateTime,
    val arrivalDateTime: DateTime,
    val segmentIds: List<String>,
    val durationInMinutes: Int,
    val stopCount: Int
)

data class DateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
)

data class Carrier (
    val name: String?,
    val allianceId: String?,
    val imageUrl: String?,
    val iata: String?,
    val icao: String?,
    val displayCode: String?
)

data class Agent(
    val name: String,
    val type: String,
    val imageUrl: String?,
    val feedbackCount: Int,
    val rating: Double,
    val ratingBreakdown: RatingBreakdown,
    val isOptimisedForMobile: Boolean
)

data class RatingBreakdown(
    val customerService: Int,
    val reliablePrices: Int,
    val clearExtraFees: Int,
    val easeOfBooking: Int,
    val other: Int
)

data class Alliance(
    val name: String
)
