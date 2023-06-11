package com.sobarna.newsapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobarna.newsapp.databinding.ContentListCategoryBinding

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var list: ArrayList<Pair<String, Boolean>> = ArrayList()

    fun submitData(list: ArrayList<Pair<String, Boolean>>){
        this.list = list
        notifyDataSetChanged()
    }

    private var categoryClick: ((Pair<String, Boolean>) -> Unit)? = null

    fun setCategoryClick(categoryClick: ((Pair<String, Boolean>) -> Unit)? = null) {
        this.categoryClick = categoryClick
    }

    private fun updateValue( position: Int) {
        list.forEachIndexed { index, pair ->
            list[index] = Pair(pair.first, index == position)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ContentListCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(pair: Pair<String, Boolean>) {
            with(binding) {
                cbCategory.text = pair.first
                cbCategory.isChecked = pair.second
                itemView.setOnClickListener {
                    categoryClick?.invoke(pair)
                    updateValue(absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContentListCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pair = list[position]
        holder.bindItem(pair)
    }
}