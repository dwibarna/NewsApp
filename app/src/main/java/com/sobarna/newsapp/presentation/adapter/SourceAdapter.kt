package com.sobarna.newsapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobarna.newsapp.databinding.ContentListCategoryBinding

class SourceAdapter: RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    private var list: List<String> = ArrayList()

    fun submitData(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }

    private var sourceClick: ((String) -> Unit)? = null

    fun setSourceClick(sourceClick: ((String) -> Unit)? = null) {
        this.sourceClick = sourceClick
    }


    inner class ViewHolder(private val binding: ContentListCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(source: String) {
            with(binding) {
                cbCategory.text = source
                cbCategory.isChecked = false
                itemView.setOnClickListener {
                    sourceClick?.invoke(source)
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