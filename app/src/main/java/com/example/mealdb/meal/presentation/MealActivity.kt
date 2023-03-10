package com.example.mealdb.meal.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.BottomSheetFragment
import com.example.mealdb.R
import com.example.mealdb.meal.domain.MealAdapter
import kotlinx.coroutines.launch

class MealActivity : AppCompatActivity() {
    lateinit var viewModel: MealViewModel
    lateinit var adapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_meal)
        val receivedCategoryName = intent.getStringExtra("categoryName")
        val receivedFlag = intent.getStringExtra("flag")
        setSupportActionBar(findViewById(R.id.appBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = when (receivedFlag) {
            "category" -> "Category: $receivedCategoryName"
            "area" -> "$receivedCategoryName cuisine"
            "ingredient" -> "Ingredient: $receivedCategoryName"
            else -> ""
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_meal)

        viewModel = ViewModelProvider(
            this, MealViewModelFactory(
                this, receivedCategoryName, receivedFlag
            )
        ).get(MealViewModel::class.java)

        lifecycleScope.launch {
            val data = viewModel.getMealEntity()
            adapter = MealAdapter(data?.meal, this@MealActivity)
            recyclerView.adapter = adapter

            val searchButton = findViewById<SearchView>(R.id.searchMealCategory)
            searchButton.setOnClickListener {
                //todo: доделать кнопку поиска
            }


            val sortButton = findViewById<ActionMenuItemView>(R.id.action_sort_meal)
            val bottomSheetFragment = BottomSheetFragment(callbackSortAscendingByName = {
                adapter.setChangedMealEntity(viewModel.interactor.sortByName(data).meal)
            }, callbackSortDescendingByName = {
                adapter.setChangedMealEntity(viewModel.interactor.sortDescendingByName(data).meal)
            })

            sortButton.setOnClickListener {
                bottomSheetFragment.show(supportFragmentManager, "bottomSheetInMealList")
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater       //что кладем в переменную?
        inflater.inflate(R.menu.overflow_menu_meal, menu)
        return true
    }

}