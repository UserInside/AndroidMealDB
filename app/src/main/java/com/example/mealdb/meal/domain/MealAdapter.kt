package com.example.mealdb.meal.domain

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealdb.R
import com.example.mealdb.recipe.presentation.RecipeActivity
import meal.data.MealList

class MealAdapter(
    val mealList: MealList?,
    context: Context
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    val mContext = context


    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageMeal)
        val textView = itemView.findViewById<TextView>(R.id.textMealName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_mealitem, parent, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val item = mealList?.meals!![position]
        Glide.with(mContext)
            .load(item.strMealThumb)
            .into(holder.imageView)
        holder.textView.text = item.strMeal
        holder.itemView.setOnClickListener {
            val recipeActivity = Intent(mContext, RecipeActivity::class.java)
            recipeActivity.putExtra("mealName", item.strMeal)
            recipeActivity.putExtra("mealId", item.idMeal)
            recipeActivity.putExtra("mealThumbnail", item.strMealThumb)
            mContext.startActivity(recipeActivity)
        }

    }

    override fun getItemCount(): Int {
        return mealList?.meals?.size ?: 0

    }

}