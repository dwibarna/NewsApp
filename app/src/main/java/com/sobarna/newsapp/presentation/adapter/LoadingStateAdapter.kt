package com.sobarna.newsapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sobarna.newsapp.databinding.ContentLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ContentLoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bindItem(loadState: LoadState) {
            with(binding) {
                if (loadState is LoadState.Error) binding.errorMsg.text =
                    loadState.error.localizedMessage
                (loadState is LoadState.Error).let {
                    errorMsg.isVisible = it
                    retryButton.isVisible = it
                }
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.setOnClickListener {
                    retry.invoke()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bindItem(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            binding = ContentLoadingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), retry = retry
        )
    }
}