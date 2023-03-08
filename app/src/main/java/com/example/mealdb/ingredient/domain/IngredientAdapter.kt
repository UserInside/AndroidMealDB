package com.example.mealdb.ingredient.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.R
import com.example.mealdb.ingredient.data.IngredientList


class IngredientAdapter(
    var ingredientList: IngredientList
) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName = itemView.findViewById<TextView>(R.id.text_ingredientName)
//        val ingredientDescription = itemView.findViewById<TextView>(R.id.text_ingredientDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_ingredientitem, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val item = ingredientList.meals
        holder.ingredientName.text = item?.get(position)?.strIngredient
//        holder.ingredientDescription.text = item?.get(position)?.strDescription
    }

    override fun getItemCount(): Int {
        return ingredientList.meals?.size ?: 0
    }


}