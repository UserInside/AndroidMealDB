package com.example.mealdb.category.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.BottomSheetFragment
import com.example.mealdb.ContentState
import com.example.mealdb.R
import com.example.mealdb.category.domain.CategoryListAdapter
import com.example.mealdb.country.presentation.CountryListFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainCategoryListActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var adapter: CategoryListAdapter
    private lateinit var viewModel: CategoryListViewModel

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        setContentView(R.layout.activity_a_category_main)

        val appBar = findViewById<Toolbar>(R.id.appBar)
        setSupportActionBar(appBar)

        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, appBar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this) //todo что тут должно быть вместо this ?
        navigationView.setCheckedItem(R.id.nav_category)

        val contentView = findViewById<View>(R.id.contentViewCategoryList)
        val progressBarView = findViewById<View>(R.id.includeProgressBar)
        val errorView = findViewById<View>(R.id.includeError)

        viewModel = ViewModelProvider(this).get(CategoryListViewModel::class.java)

        lifecycleScope.launch {

            viewModel.stateFlow.onEach { state ->
                when (state.contentState) {
                    ContentState.Idle,
                    ContentState.Loading -> {
                        contentView.visibility = View.GONE
                        progressBarView.visibility = View.VISIBLE
                        errorView.visibility = View.GONE
                    }
                    ContentState.Done -> {
                        contentView.visibility = View.VISIBLE
                        progressBarView.visibility = View.GONE
                        errorView.visibility - View.GONE
                    }
                    ContentState.Error -> {
                        contentView.visibility = View.GONE
                        progressBarView.visibility = View.GONE
                        errorView.visibility = View.VISIBLE
                    }

                }

                //create recyclerview
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_category)
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = LinearLayoutManager(this@MainCategoryListActivity)
                adapter = CategoryListAdapter(
                    state.categoryListEntity?.categoryList,
                    this@MainCategoryListActivity
                )
                recyclerView.adapter = adapter

                //search button and view in toolbar
                val searchView = findViewById<SearchView>(R.id.searchViewCategory)
                val searchButton = findViewById<ActionMenuItemView>(R.id.action_search_category)
                searchButton?.setOnClickListener {
                    if (!searchView.isVisible) {
                        searchView.visibility = View.VISIBLE
                    } else {
                        adapter.setChangedCategoryEntity(state.categoryListEntity?.categoryList)
                        searchView.visibility = View.INVISIBLE
                    }
                }

                //search logic
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        adapter.setChangedCategoryEntity(
                            viewModel.getFilteredCategoryList(query).categoryList
                        )
                        return true
                    }
                })


                val bottomSheetFragment = BottomSheetFragment(
                    callbackSortAscendingByName = {
                        adapter.setChangedCategoryEntity(
                            viewModel.getCategoryListSortedAscendingByName().categoryList
                        )
                    },
                    callbackSortDescendingByName = {
                        adapter.setChangedCategoryEntity(
                            viewModel.getCategoryListSortedDescendingByName().categoryList
                        )
                    }
                )
                val sortButton = findViewById<ActionMenuItemView>(R.id.action_sort_category)
                sortButton?.setOnClickListener {
                    bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
                }
            }.launchIn(lifecycleScope)
        }



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                } else {
//                    this.remove()
                    finish()
//                     попробовать найти функцию "назад по дефолту"
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu_category, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val areaFragment = supportFragmentManager.findFragmentByTag("AreaFragment")

        when (item.itemId) {
            R.id.nav_area -> {
                if (areaFragment != null && areaFragment.isVisible) {
                    drawer.closeDrawer(GravityCompat.START)
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.contentViewCategoryList,
                            CountryListFragment(),
                            "AreaFragment"
                        )
                        .commit()
                }
            }
            R.id.nav_category -> {
                if (areaFragment != null) {
                    if (areaFragment.isVisible) {

                        supportFragmentManager.beginTransaction().remove(areaFragment).commit()
                        //todo сделать возврат к активити. Активити вроде возвращает, но тулбар ломается
                    } else {
                        drawer.closeDrawer(GravityCompat.START)
                    }
                } else {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.theme_light -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                this.recreate()
                Log.i("radio", "Light clicked")
                drawer.closeDrawer(GravityCompat.START)

            }
            R.id.theme_dark -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                recreate()
                Log.i("radio", "Dark clicked")
                drawer.closeDrawer(GravityCompat.START)
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true

    }
}

// после переключения темы перестает работать тулбар... хм...
