package fr.eilco.flycompare.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.eilco.flycompare.R
import fr.eilco.flycompare.models.Suggestion

class HomeAdapter(private val countries: List<Suggestion>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>()  {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardTown: TextView = view.findViewById<TextView>(R.id.card_town)
        val cardCountry: TextView = view.findViewById<TextView>(R.id.card_country)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.good_deals_card_item, parent, false);
        return ViewHolder(view);
    }

    override fun getItemCount(): Int = countries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = countries[position]
        holder.cardTown.text = suggestion.name
        holder.cardCountry.text = suggestion.context?.region?.name ?: suggestion.context?.country?.name
    }
}