package com.example.mealdb.category.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import category.data.CategoryHttpClient
import category.data.CategoryListGatewayImplementation
import category.domain.CategoryListInteractor
import com.example.mealdb.BottomSheetFragment
import com.example.mealdb.ContentState
import com.example.mealdb.R
import com.example.mealdb.category.domain.CategoryListAdapter
import com.example.mealdb.country.presentation.CountryListFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainCategoryListActivity
    : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var drawer: DrawerLayout? = null
    private var adapter: CategoryListAdapter? = null

    //TODO https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
    private val viewModel: CategoryListViewModel by viewModels {
        CategoryListViewModel.factory(
            CategoryListInteractor(
                CategoryListGatewayImplementation(
                    CategoryHttpClient()
                )
            )
        )
    }

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
        drawer?.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this) //todo что тут должно быть вместо this ?
        navigationView.setCheckedItem(R.id.nav_category)

        val contentView = findViewById<View>(R.id.contentViewCategoryList)
        val progressBarView = findViewById<View>(R.id.includeProgressBar)
        val errorView = findViewById<View>(R.id.includeError)
        val buttonRetry = findViewById<View>(R.id.buttonRetry)

        //create recyclerview
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_category)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this@MainCategoryListActivity)
        adapter = CategoryListAdapter(
            null,
            this@MainCategoryListActivity
        )
        recyclerView.adapter = adapter

        buttonRetry.setOnClickListener {
            viewModel.fetchData()
        }

        lifecycleScope.launch {
            //TODO https://developer.android.com/topic/libraries/architecture/viewmodel#implement-viewmodel
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect {state ->
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
                    adapter?.setChangedCategoryEntity(state.categoryListEntity?.categoryList)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawer?.isDrawerOpen(GravityCompat.START) == true) {
                    drawer?.closeDrawer(GravityCompat.START)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search_category -> {
                val searchView = findViewById<SearchView>(R.id.searchViewCategory)

                if (!searchView.isVisible) {
                    searchView.visibility = View.VISIBLE
                } else {
                    searchView.visibility = View.INVISIBLE
                }

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        adapter?.setChangedCategoryEntity(
                            viewModel.getFilteredCategoryList(query).categoryList
                        )
                        return true
                    }
                })
                true
            }
            R.id.action_sort_category -> {
                val bottomSheetFragment = BottomSheetFragment(
                    callbackSortAscendingByName = {
                        adapter?.setChangedCategoryEntity(
                            viewModel.getCategoryListSortedAscendingByName().categoryList
                        )
                    },
                    callbackSortDescendingByName = {
                        adapter?.setChangedCategoryEntity(
                            viewModel.getCategoryListSortedDescendingByName().categoryList
                        )
                    }
                )
                bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val areaFragment = supportFragmentManager.findFragmentByTag("AreaFragment")

        when (item.itemId) {
            R.id.nav_area -> {
                if (areaFragment != null && areaFragment.isVisible) {
                    drawer?.closeDrawer(GravityCompat.START)
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
                    } else {
                        drawer?.closeDrawer(GravityCompat.START)
                    }
                } else {
                    drawer?.closeDrawer(GravityCompat.START)
                }
            }
            R.id.theme_light -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                recreate()
                drawer?.closeDrawer(GravityCompat.START)

            }
            R.id.theme_dark -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                recreate()
                drawer?.closeDrawer(GravityCompat.START)
            }
        }

        drawer?.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        drawer = null
        adapter = null
        super.onDestroy()
    }
}

// после переключения темы перестает работать тулбар... хм...
