package com.example.flycompareapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.flycompareapp.R

class HomeCardAdapter : RecyclerView.Adapter<HomeCardAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val homeGoodDealsCards = view.findViewById<RelativeLayout>(R.id.home_good_deals_cards)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.good_deals_card_item, parent, false);
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return 5;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
}