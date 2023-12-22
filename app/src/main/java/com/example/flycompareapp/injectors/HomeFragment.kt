@file:Suppress("DEPRECATION")

package com.example.flycompareapp.injectors

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flycompareapp.R
import com.example.flycompareapp.adapters.HomeCardAdapter

class HomeFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.home_screen, container, false);

        val horizontalRecyclerView = view?.findViewById<RecyclerView>(R.id.home_good_deals_cards)
        horizontalRecyclerView?.adapter = HomeCardAdapter()

        return view;
    }
}