package com.example.mealdb.meal.domain

import android.app.Activity
import android.app.ActivityOptions
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
import com.example.mealdb.meal.presentation.MealListActivity
import com.example.mealdb.recipe.presentation.RecipeActivity
import meal.data.MealList

class MealListAdapter(
    var mealList: MealList?,
    context: Context
) : RecyclerView.Adapter<MealListAdapter.MealViewHolder>() {

    val mContext = context

    fun setChangedMealEntity(mealList: MealList?) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.mealImage)
        val textView = itemView.findViewById<TextView>(R.id.textMealName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_mealitem, parent, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val item = mealList?.meals!![position]
        val image = item.strMealThumb
        Glide.with(mContext)
            .load("${item.strMealThumb}/preview")
            .placeholder(R.drawable.baseline_hourglass_bottom_24_black)
            .error(R.drawable.baseline_block_24_black)
            .fallback(R.drawable.baseline_visibility_off_24_black)
            .into(holder.imageView)
        holder.textView.text = item.strMeal
        holder.itemView.setOnClickListener {
            val recipeActivity = Intent(mContext, RecipeActivity::class.java)
            recipeActivity.putExtra("mealId", item.idMeal)
            recipeActivity.putExtra("mealThumbnail", (item.strMealThumb))
            val options = ActivityOptions.makeSceneTransitionAnimation(mContext as Activity, holder.imageView, "mealImage")
            mContext.startActivity(recipeActivity, options.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return mealList?.meals?.size ?: 0

    }

}