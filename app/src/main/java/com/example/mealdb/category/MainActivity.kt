package com.example.mealdb.category

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import category.data.CategoryGatewayImplementation
import category.data.CategoryHttpClient
import category.domain.CategoryGateway
import category.domain.CategoryInteractor
import com.example.mealdb.R
import com.example.mealdb.category.domain.CategoryAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/*
add close filter in category
add sort in category

 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_a_category_main)

        setSupportActionBar(findViewById(R.id.appBar))


        //Data Layer
        val categoryHttpClient = CategoryHttpClient()
        val gateway: CategoryGateway = CategoryGatewayImplementation(categoryHttpClient)
        //Domain Layer
        val interactor = CategoryInteractor(gateway)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_category)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val progressBar = findViewById<View>(R.id.includeProgressBar)
        val error = findViewById<View>(R.id.includeError)

        try {
            lifecycleScope.launch {
                progressBar.isVisible = true
                error.isVisible = false
                recyclerView.isVisible = false

                delay(100)
                val data = interactor.fetchData()

                val adapter = CategoryAdapter(data.categoryList, this@MainActivity)
                recyclerView.adapter = adapter
                progressBar.isVisible = false
                recyclerView.isVisible = true

                val searchView = findViewById<SearchView>(R.id.searchViewCategory)
                val searchButton = findViewById<ActionMenuItemView>(R.id.action_search_category)
                searchButton.setOnClickListener {
                    if (!searchView.isVisible) {
                        searchView.visibility = View.VISIBLE
                    } else {
                        adapter.setChangedCategoryEntity(data.categoryList)
                        searchView.visibility = View.INVISIBLE
                    }

                }

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        adapter.setChangedCategoryEntity(
                            interactor.filterCategoryList(newText, data).categoryList
                        )
                        return true
                    }
                }
                )

                val sortButton = findViewById<ActionMenuItemView>(R.id.action_sort_category)
                var sortedByName: Boolean = false
                sortButton.setOnClickListener {
                    if (!sortedByName) {
                        adapter.setChangedCategoryEntity(
                            interactor.sortByName(data).categoryList
                        )
                        sortedByName = true
                    } else {
                        adapter.setChangedCategoryEntity(
                            interactor.sortDescendingByName(data).categoryList
                        )
                        sortedByName = false
                    }


                }

            }
        } catch (throwable: Throwable) {
            progressBar.isVisible = false
            error.isVisible = true
            recyclerView.isVisible = false

            //TODO доделать, чтобы не вылетало при отключении сети
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu_category, menu)
        return true
    }
}
