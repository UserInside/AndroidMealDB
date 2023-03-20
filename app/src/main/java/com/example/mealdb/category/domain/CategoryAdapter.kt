package com.example.mealdb.category.domain

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import category.data.CategoryList
import com.bumptech.glide.Glide
import com.example.mealdb.R
import com.example.mealdb.meal.presentation.MealListActivity


class CategoryAdapter(var categoryList: CategoryList?, context: Context) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    val mContext = context

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.textCategoryName)
        val imageCategory: ImageView = itemView.findViewById(R.id.imageCategory)
        val categoryDescription: TextView = itemView.findViewById(R.id.textCategoryDescription)

        fun bind() {
        }
    }

    fun setChangedCategoryEntity(categoryList: CategoryList?) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

//    fun setSortedCategory(catergoryList: CategoryList?) {
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_categoryitem, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categoryList?.categories?.get(position)
        holder.categoryName.text = item?.strCategory
        Glide
            .with(mContext)
            .load(item?.strCategoryThumb)
            .placeholder(R.drawable.baseline_hourglass_bottom_24_black)
            .error(R.drawable.baseline_block_24_black)
            .fallback(R.drawable.baseline_visibility_off_24_black)
            .into(holder.imageCategory)

        holder.categoryDescription.text = item?.strCategoryDescription

        holder.itemView.setOnClickListener {
            val mealListActivity = Intent(mContext, MealListActivity::class.java)
            mealListActivity.putExtra("categoryName", item?.strCategory)
            mealListActivity.putExtra("flag", "category")
            mContext.startActivity(mealListActivity)
        }
    }

    override fun getItemCount(): Int {
        return categoryList?.categories?.size ?: 0
    }
}