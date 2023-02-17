package com.example.mealdb.receipt.domain

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.R
import receipt.data.ReceiptItem

class TagsAdapter(
    val tags: List<String>?
) : RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

    class TagsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_tags, parent, false)
        return TagsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.textView.text = tags?.get(position) ?: ""
    }

    override fun getItemCount(): Int {
        return tags?.size ?: 0
    }
}