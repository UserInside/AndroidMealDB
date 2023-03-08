package com.example.mealdb.country.domain

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.R
import com.example.mealdb.country.data.CountryList
import com.example.mealdb.meal.presentation.Activity_B_Meal

class CountryAdapter(var countryList : CountryList, context: Context) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    val mContext = context

    class CountryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val countryName : TextView = itemView.findViewById(R.id.text_countryName)
    }

    fun setChangedCountryAdapter(countryList: CountryList){
        this.countryList = countryList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_countryitem, parent, false)
        return CountryViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val item = countryList.meals
        holder.countryName.text = item?.get(position)?.strArea ?: ""
        holder.itemView.setOnClickListener {
            val mealActivity = Intent(mContext, Activity_B_Meal::class.java)
            mealActivity.putExtra("categoryName", countryList.meals?.get(position)?.strArea)
            mealActivity.putExtra("flag", "area")
            mContext.startActivity(mealActivity)
        }
    }

    override fun getItemCount(): Int {
        return countryList.meals?.size ?: 0
    }
}