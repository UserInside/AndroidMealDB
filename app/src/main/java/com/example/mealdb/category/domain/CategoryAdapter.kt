package com.example.mealdb.category.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import category.data.CategoryItem
import category.data.CategoryList
import com.example.mealdb.R


class CategoryAdapter(
//    private val names: List<String>
    val categoryList: CategoryList?
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
// написано: Создадим адаптер - наследуем наш класс от класса RecyclerView.Adapter
// и в качестве параметра указываем созданный нами MyViewHolder. Студия попросит реализовать три метода.
// Что это за конструкция такая?

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val categoryName: TextView = itemView.findViewById(R.id.textCategoryName)
//        val imageCategory : ImageView = itemView.findViewById(R.id.imageCategory)
        val categoryDescription: TextView = itemView.findViewById(R.id.textCategoryDescription)

        fun bind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_categoryitem, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.categoryName.text = categoryList?.categories!![position].strCategory
        holder.categoryDescription.text = categoryList.categories[position].strCategoryDescription
//        holder.categoryName.text = names[position]
//        holder.categoryDescription.text = "кот"
//        holder.categoryName.text = categoryList?.categories!![position].strCategory
//        holder.imageCategory = categoryList!!.categories[position]
//        holder.categoryDescription.text = categoryList.categories[position].strCategoryDescription
    }

    override fun getItemCount(): Int {
//        return names.size
        return categoryList?.categories!!.size   // Вопрос?? можно !! ставить или лучше ?: 0
    }
}