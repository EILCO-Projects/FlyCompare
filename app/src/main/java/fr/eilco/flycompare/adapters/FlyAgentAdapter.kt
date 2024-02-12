package fr.eilco.flycompare.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import fr.eilco.flycompare.FlightActivity
import fr.eilco.flycompare.FlightDetailsActivity
import fr.eilco.flycompare.R
import fr.eilco.flycompare.models.Agent
import fr.eilco.flycompare.models.Carrier
import fr.eilco.flycompare.models.DateTime
import fr.eilco.flycompare.models.Item
import fr.eilco.flycompare.models.Results
import fr.eilco.flycompare.utils.Helpers
import fr.eilco.flycompare.utils.Keys
import fr.eilco.flycompare.viewmodels.MyViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FlyAgentAdapter(
    private val result: Results?,
    private val activity: Activity,
    private val context: Context
) :
    RecyclerView.Adapter<FlyAgentAdapter.ViewHolder>() {

    val agents = formatData()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageCtrller = view.findViewById<ImageView>(R.id.agent_image)
        val nameCtrller = view.findViewById<TextView>(R.id.agent_name)
        val ratingCtrller = view.findViewById<TextView>(R.id.agent_rating)
        val typeCtrller = view.findViewById<TextView>(R.id.agent_type)
        val reviewers = view.findViewById<TextView>(R.id.agent_reviewers)
        val redirection = view.findViewById<Button>(R.id.site)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.flights_agent_item, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agent = agents[position]
        Glide.with(activity)
            .load(agent.imageUrl)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)) // Optional: cache strategy
            .placeholder(R.drawable.airfrance_logo) // Optional: placeholder image while loading
            .error(R.drawable.airfrance_logo) // Optional: image to display if loading fails
            .into(holder.imageCtrller)
        holder.nameCtrller.text = agent.name
        holder.reviewers.text = agent.feedbackCount.toString()
        holder.ratingCtrller.text =
            agent.rating.toString()
        holder.typeCtrller.text = formatAgentType(agent.type)
        holder.redirection.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Keys.DEFAULT_LINK))
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int = agents.size

    private fun formatData(): List<Agent> {
        val ag = ArrayList<Agent>()
        result?.agents?.entries?.forEach { (key, value) ->
            run {
                ag.add(value)
            }
        }
        return ag
    }

    private fun formatAgentType(type: String): String {
        if(type == "AGENT_TYPE_AIRLINE") {
            return "COMPAGNIE AERIENNE"
        } else if (type == "AGENT_TYPE_TRAVEL_AGENT") {
            return "AGENCE DE VOYAGE"
        }
        return "COMPAGNIE AERIENNE"
    }
}
