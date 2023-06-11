package com.sobarna.newsapp.presentation.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.google.android.flexbox.*
import com.sobarna.newsapp.R
import com.sobarna.newsapp.data.Resource
import com.sobarna.newsapp.databinding.ActivityMainBinding
import com.sobarna.newsapp.presentation.adapter.CategoryAdapter
import com.sobarna.newsapp.presentation.adapter.SourceAdapter
import com.sobarna.newsapp.presentation.news.NewsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sourceAdapter: SourceAdapter
    private var selectedCategory: String = "all"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCategoryAdapter()
        initSearch()
    }

    private fun initSearch() {
        binding.searchView.apply {
            queryHint = "Search News..."
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (binding.checkbox.isChecked) {
                        if (query.isNullOrBlank().not()) {
                            initSearchSourceAdapter(query!!)
                        }
                    } else {
                        if (query.isNullOrBlank().not()) {
                            Intent(this@MainActivity, NewsActivity::class.java).apply {
                                putExtra(Intent.EXTRA_SUBJECT, query)
                            }.let(::startActivity)
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            progressBar.isVisible = state
            rvSources.isVisible = !state
        }
    }

    private fun initSearchSourceAdapter(query: String) {
        viewModel.getAllSourcesFromSearch(query).observe(this) { value ->
            when (value) {
                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        applicationContext,
                        value.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    val listSource: ArrayList<String> = ArrayList()
                    listSource.clear()
                    value.data?.forEach {
                        listSource.add(it.name)
                    }
                    initCategoryAdapter()
                    sourceAdapter = SourceAdapter()
                    sourceAdapter.submitData(listSource.distinct())
                    binding.rvSources.apply {
                        adapter = sourceAdapter
                        layoutManager =
                            FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP).apply {
                                alignItems = AlignItems.CENTER
                                justifyContent = JustifyContent.CENTER
                            }
                    }
                    sourceAdapter.setSourceClick(
                        sourceClick = {
                            Intent(this@MainActivity, NewsActivity::class.java).apply {
                                putExtra(Intent.EXTRA_TEXT, it) // source
                            }.let(::startActivity)
                        })
                }
            }
        }
    }

    private fun initCategoryAdapter() {
        val listCategory = listOf(*resources.getStringArray(R.array.list_category))
        val listPair = listCategory.mapIndexed { index, s ->
            Pair(s, index == 0)
        } as ArrayList<Pair<String, Boolean>>
        categoryAdapter = CategoryAdapter()
        categoryAdapter.submitData(listPair)
        binding.rvCategory.apply {
            adapter = categoryAdapter
            layoutManager =
                FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP).apply {
                    alignItems = AlignItems.CENTER
                    justifyContent = JustifyContent.CENTER
                }
        }
        categoryAdapter.setCategoryClick(
            categoryClick = { category ->
                selectedCategory = category.first
                initSourceAdapter(category.first)
            }
        )
    }

    private fun initSourceAdapter(category: String) {
        viewModel.getAllSourcesFromCategory(category).observe(this) { value ->
            when (value) {
                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        applicationContext,
                        value.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    val listSource: ArrayList<String> = ArrayList()
                    listSource.clear()
                    value.data?.forEach {
                        listSource.add(it.idSource)
                    }
                    sourceAdapter = SourceAdapter()
                    sourceAdapter.submitData(listSource.distinct())
                    binding.rvSources.apply {
                        adapter = sourceAdapter
                        layoutManager =
                            FlexboxLayoutManager(
                                context,
                                FlexDirection.ROW,
                                FlexWrap.WRAP
                            ).apply {
                                alignItems = AlignItems.CENTER
                                justifyContent = JustifyContent.CENTER
                            }
                    }
                    sourceAdapter.setSourceClick(
                        sourceClick = {
                            Intent(
                                this@MainActivity,
                                NewsActivity::class.java
                            ).apply {
                                putExtra(Intent.EXTRA_TEXT, it)
                            }.let(::startActivity)
                        })
                }
            }
        }
    }
}