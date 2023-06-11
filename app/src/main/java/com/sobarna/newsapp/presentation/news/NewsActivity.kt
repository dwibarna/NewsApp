package com.sobarna.newsapp.presentation.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.sobarna.newsapp.databinding.ActivityNewsBinding
import com.sobarna.newsapp.presentation.adapter.LoadingStateAdapter
import com.sobarna.newsapp.presentation.adapter.NewsAdapter
import com.sobarna.newsapp.presentation.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListNews()
    }

    private fun showLoading() {
        viewModel.onLoading.observe(this) { state ->
            binding.progressBar.isVisible = state
            binding.rvNews.isVisible = !state
        }
    }

    private fun initListNews() {
        val query = intent.getStringExtra(Intent.EXTRA_SUBJECT)
        val sources = intent.getStringExtra(Intent.EXTRA_TEXT)
        newsAdapter = NewsAdapter()

        viewModel.getAllNews(query, sources)
        showLoading()

        viewModel.newsLiveData.observe(this) { pagingData ->
            newsAdapter.submitData(lifecycle, pagingData)
        }

        binding.rvNews.apply {
            adapter = newsAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter(
                    retry = {
                        newsAdapter.retry()
                    }
                )
            )
            layoutManager = LinearLayoutManager(context)
        }
        newsAdapter.setActionClick(
            actionClick = {
                Intent(this@NewsActivity, DetailActivity::class.java).apply {
                    putExtra(Intent.EXTRA_INTENT, it.url)
                }.let(::startActivity)
            }
        )
    }
}