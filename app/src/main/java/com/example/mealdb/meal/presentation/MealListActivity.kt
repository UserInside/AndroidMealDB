package com.example.mealdb.meal.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.BottomSheetFragment
import com.example.mealdb.ContentState
import com.example.mealdb.R
import com.example.mealdb.meal.domain.MealListAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MealListActivity : AppCompatActivity() {
    lateinit var viewModel: MealListViewModel
    lateinit var adapter: MealListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_meal)
        val receivedCategoryName = intent.getStringExtra("categoryName")
        val receivedFlag = intent.getStringExtra("flag")
        val appBar = findViewById<Toolbar>(R.id.appBar2)
        setSupportActionBar(appBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = when (receivedFlag) {
            "category" -> "Category: $receivedCategoryName"
            "area" -> "$receivedCategoryName cuisine"
            "ingredient" -> "Ingredient: $receivedCategoryName"
            else -> ""
        }

        val errorView = findViewById<View>(R.id.includeError)
        val progressView = findViewById<View>(R.id.includeProgressBar)
        val contentView = findViewById<View>(R.id.contentViewMealList)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_meal)

        viewModel = ViewModelProvider(
            this, MealListViewModelFactory(
                this, receivedCategoryName, receivedFlag)
        ).get(MealListViewModel::class.java)

        lifecycleScope.launch {
            viewModel.stateFlow.onEach { state ->
                when (state.contentState) {
                    ContentState.Idle,
                    ContentState.Loading -> {
                        progressView.visibility = View.VISIBLE
                        errorView.visibility = View.GONE
                        contentView.visibility = View.GONE
                    }
                    ContentState.Error -> {
                        progressView.visibility = View.GONE
                        errorView.visibility = View.VISIBLE
                        contentView.visibility = View.GONE
                    }
                    ContentState.Done -> {
                        progressView.visibility = View.GONE
                        errorView.visibility = View.GONE
                        contentView.visibility = View.VISIBLE
                    }
                }

                adapter = MealListAdapter(state.mealListEntity?.mealList, this@MealListActivity)
                recyclerView.adapter = adapter

                val searchView = findViewById<SearchView>(R.id.searchViewMealList)
                val fuckingSearchButton = findViewById<ActionMenuItemView>(R.id.action_search_meal)
                fuckingSearchButton?.setOnClickListener { //разобраться почему не работает без знака вопроса
                    if (!searchView.isVisible) {
                        searchView.visibility = View.VISIBLE
                    } else {
                        adapter.setChangedMealEntity(state.mealListEntity?.mealList)
                        searchView.visibility = View.INVISIBLE
                    }
                }
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        adapter.setChangedMealEntity(
                            viewModel.getMealListSearched(newText)?.mealList
                        )
                        return true
                    }
                })

                val sortButton = findViewById<ActionMenuItemView>(R.id.action_sort_meal)
                val bottomSheetFragment = BottomSheetFragment(
                    callbackSortAscendingByName = {
                        adapter.setChangedMealEntity(viewModel.getMealListEntitySortedAscendingByName()?.mealList)},
                    callbackSortDescendingByName = {
                        adapter.setChangedMealEntity(viewModel.getMealListEntitySortedDescendingByName()?.mealList)
                    })

                sortButton?.setOnClickListener {
                    bottomSheetFragment.show(supportFragmentManager, "bottomSheetInMealList")
                }

            }.launchIn(lifecycleScope)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater       //что кладем в переменную?
        inflater.inflate(R.menu.overflow_menu_meal, menu)
        return true
    }

}