package com.example.mealdb.category.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
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
import com.example.mealdb.R
import com.example.mealdb.category.domain.CategoryAdapter
import com.example.mealdb.country.presentation.CountryListFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var adapter: CategoryAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_a_category_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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

                delay(50)
                val data = viewModel.getCategoryEntity()

                adapter = CategoryAdapter(data.categoryList, this@MainActivity)
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
                            viewModel.interactor.filterCategoryList(newText, data).categoryList
                        )
                        return true
                    }
                }
                )
                val bottomSheetFragment = BottomSheetFragment(
                    callbackSortAscendingByName = {
                        adapter.setChangedCategoryEntity(
                            viewModel.interactor.sortByName(data).categoryList)
                    },
                    callbackSortDescendingByName = {
                        adapter.setChangedCategoryEntity(
                            viewModel.interactor.sortDescendingByName(data).categoryList
                        )
                    }
                )
                val sortButton = findViewById<ActionMenuItemView>(R.id.action_sort_category)
                sortButton.setOnClickListener {
                    bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
                }




            }
        } catch (throwable: Throwable) {
            progressBar.isVisible = false
            error.isVisible = true
            recyclerView.isVisible = false

            //TODO доделать, чтобы не вылетало при отключении сети
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
        when (item.itemId) {
            R.id.nav_area -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.category_framelayout, CountryListFragment()).commit()
            }
            R.id.nav_category -> {
                //TODO дописать возврат к категориям
            }
            R.id.nav_ingredients -> {
                Toast.makeText(this, "I'm not ready!", Toast.LENGTH_SHORT).show()
                //не понятно, где искать название ингредиента в формате, в котором хочет api
//                supportFragmentManager.beginTransaction().replace(R.id.category_framelayout, IngredientListFragment()).commit()
            }
            else -> {
                drawer.closeDrawer(GravityCompat.START)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}
