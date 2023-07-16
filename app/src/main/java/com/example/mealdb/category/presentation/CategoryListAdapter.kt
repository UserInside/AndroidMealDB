package com.example.mealdb.category.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.mealdb.R
import com.example.mealdb.category.data.CategoryItem

// TODO
// стараемся не прокидывать контекст
// стараемся не не прокидывать логику на clicklistener а выносим логику, выше на уровень
//
class CategoryListAdapter(
    private val rm: RequestManager,
    private val onClickListener: (String) -> Unit,
) : RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {
    private val list: MutableList<CategoryItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_categoryitem, parent, false)
        return CategoryViewHolder(
            itemView = itemView,
            rm = rm,
            onClickListener = onClickListener,
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        list.getOrNull(position)?.let { holder.bind(it) }
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        val newItem = payloads.firstOrNull { it is CategoryItem } as? CategoryItem
        val item = list.getOrNull(position)
        when {
            newItem != null -> holder.bind(newItem)
            item != null -> holder.bind(item)
        }
    }

    override fun onViewRecycled(holder: CategoryViewHolder) {
        holder.onClear()
    }

    override fun getItemCount(): Int = list.size

    fun update(newList: List<CategoryItem>) {
        val diffUtilCallback = DiffUtilCallback(
            oldList = list,
            newList = newList,
        )
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
        list.clear()
        list.addAll(newList)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    class CategoryViewHolder(
        itemView: View,
        private val rm: RequestManager,
        private val onClickListener: (String) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.textCategoryName)
        private val imageCategory: ImageView = itemView.findViewById(R.id.imageCategory)
        private val categoryDescription: TextView =
            itemView.findViewById(R.id.textCategoryDescription)

        fun bind(item: CategoryItem) {
            categoryName.text = item.strCategory
            rm.load(item.strCategoryThumb)
                .placeholder(R.drawable.baseline_hourglass_bottom_24_black)
                .error(R.drawable.baseline_block_24_black)
                .fallback(R.drawable.baseline_visibility_off_24_black)
                .into(imageCategory)

            categoryDescription.text = item.strCategoryDescription
            item.strCategory?.let { category ->
                itemView.setOnClickListener { onClickListener(category) }
            }
        }

        fun onClear() {
            categoryName.text = null
            imageCategory.setImageResource(0)
            categoryDescription.text = null
        }
    }
}

private class DiffUtilCallback(
    private val oldList: List<CategoryItem>,
    private val newList: List<CategoryItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList.getOrNull(oldItemPosition)?.idCategory == newList.getOrNull(newItemPosition)?.idCategory

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any =
        newList[newItemPosition]
}