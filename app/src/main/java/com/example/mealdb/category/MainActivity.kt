package com.example.mealdb.category

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import category.data.CategoryGatewayImplementation
import category.data.CategoryHttpClient
import category.domain.CategoryGateway
import category.domain.CategoryInteractor
import com.example.mealdb.R
import com.example.mealdb.category.domain.CategoryAdapter
import kotlinx.coroutines.launch
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

/*
add tags
add county
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
                recyclerView.adapter = CategoryAdapter(data.categoryList, this@MainActivity)
                progressBar.isVisible = false
                recyclerView.isVisible = true

            }
        } catch (throwable: Throwable) {
            progressBar.isVisible = false
            error.isVisible = true
            recyclerView.isVisible = false

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        return true
    }
}
