package com.example.mealdb.ingredient.domain

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.R
import com.example.mealdb.ingredient.data.IngredientList
import com.example.mealdb.meal.presentation.Activity_B_Meal


class IngredientAdapter(
    var ingredientList: IngredientList,
    val context : Context
) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    val mContext = context

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

        holder.itemView.setOnClickListener{
            val mealActivity = Intent(mContext, Activity_B_Meal::class.java)
            mealActivity.putExtra("caterogyName", item?.get(position)?.strIngredient)
            mealActivity.putExtra("flag", "ingredient")
            mContext.startActivity(mealActivity)
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.meals?.size ?: 0
    }


}