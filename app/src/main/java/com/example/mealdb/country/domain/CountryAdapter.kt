package com.example.mealdb.country.domain

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.R
import com.example.mealdb.country.data.CountryList

class CountryAdapter(var countryList : CountryList) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val countryName : TextView = itemView.findViewById(R.id.text_countryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_countryitem, parent, false)
        return CountryViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val item = countryList.meals
        holder.countryName.text = item?.get(position)?.strArea ?: ""

    }

    override fun getItemCount(): Int {
        return countryList.meals?.size ?: 0
    }
}