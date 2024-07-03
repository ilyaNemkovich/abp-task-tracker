package com.arnas.abp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(var items: List<Item>, private val itemClickListener: (Item) -> Unit) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val subtitleTextView: TextView = itemView.findViewById(R.id.subtitleTextView)
        private val imageViewDone: ImageView = itemView.findViewById(R.id.imageView_done)

        fun bind(item: Item) {
            titleTextView.text = item.title
            subtitleTextView.text = item.subtitle
            imageViewDone.isGone = item.isCompleted.not()

            itemView.setOnClickListener {
                itemClickListener(item)
            }
        }
    }
}

data class Item(val id: Int, val title: String, val subtitle: String, val password: String, val isCompleted: Boolean)