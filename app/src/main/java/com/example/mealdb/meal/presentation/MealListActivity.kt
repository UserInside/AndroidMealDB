package com.example.mealdb.meal.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

class MealListActivity : AppCompatActivity() {
    lateinit var viewModel: MealListViewModel
    lateinit var adapter: MealListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_b_meal)


        val receivedFlag = when (intent.data?.queryParameterNames?.first()) {
            "c" -> "category"
            "a" -> "area"
            "i" -> "ingredient"
            else -> intent.getStringExtra("flag") ?: ""
        }
        val categoryName = intent.data?.getQueryParameter(intent.data?.queryParameterNames?.first())
        val receivedCategoryName = categoryName ?: intent.getStringExtra("categoryName") ?: ""


        setSupportActionBar(findViewById<Toolbar>(R.id.appBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = when (receivedFlag) {
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
                this, receivedCategoryName, receivedFlag
            )
        ).get(MealListViewModel::class.java)


        viewModel.stateFlow.onEach { state ->
            when (state.contentState) {
                ContentState.Idle, ContentState.Loading -> {
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

        }.launchIn(lifecycleScope)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater = menuInflater       //что кладем в переменную?
        menuInflater.inflate(R.menu.overflow_menu_meal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share_meal -> {
                var prefix = ""
                when (viewModel.flag) {
                    "category" -> prefix = "c="
                    "area" -> prefix = "a="
                    "ingredient" -> prefix = "i="

                }
                val categoryName = viewModel.categoryName
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://www.themealdb.com/api/json/v1/1/filter.php?$prefix$categoryName"
                )
                startActivity(Intent.createChooser(shareIntent, "seems it works"))
            }
            R.id.action_search_meal -> {
                val searchView = findViewById<SearchView>(R.id.searchViewMealList)
                if (!searchView.isVisible) {
                    searchView.visibility = View.VISIBLE
                    searchView.isIconified = false
                } else {
                    searchView.visibility = View.INVISIBLE
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
            }
            R.id.action_sort_meal -> {
                val bottomSheetFragment = BottomSheetFragment(callbackSortAscendingByName = {
                    adapter.setChangedMealEntity(viewModel.getMealListEntitySortedAscendingByName()?.mealList)
                }, callbackSortDescendingByName = {
                    adapter.setChangedMealEntity(viewModel.getMealListEntitySortedDescendingByName()?.mealList)
                })

                bottomSheetFragment.show(supportFragmentManager, "bottomSheetInMealList")
            }
        }
        return super.onOptionsItemSelected(item)
    }

}