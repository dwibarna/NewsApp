package com.sobarna.newsapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sobarna.newsapp.R
import com.sobarna.newsapp.databinding.ContentListNewsBinding
import com.sobarna.newsapp.domain.model.News
import com.sobarna.newsapp.util.Utils

class NewsAdapter :
    PagingDataAdapter<News, NewsAdapter.ViewHolder>(DiffCallBack) {

    private var actionClick: ((News) -> Unit)? = null

    fun setActionClick(actionClick: ((News) -> Unit)? = null) {
        this.actionClick = actionClick
    }

    inner class ViewHolder(private val binding: ContentListNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(news: News) {
            with(binding){
                tvTitle.text = HtmlCompat.fromHtml(news.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
                tvDesc.text = news.description
                tvPublish.text = Utils.formatDateTime(news.publishedAt)

                Glide.with(itemView.context)
                    .asBitmap()
                    .placeholder(R.drawable.news_placeholder)
                    .apply(RequestOptions().centerCrop())
                    .load(news.urlToImage)
                    .into(ivImageNews)

                itemView.setOnClickListener {
                    actionClick?.invoke(news)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContentListNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindItem(it)
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

    }
}