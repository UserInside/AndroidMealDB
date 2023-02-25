package com.example.mealdb.meal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionBarContextView
import androidx.appcompat.widget.ActionMenuView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.receipt.Activity_C_Recipe
import com.example.mealdb.R
import com.example.mealdb.meal.domain.MealAdapter
import kotlinx.coroutines.launch
import meal.data.MealGatewayImplementation
import meal.data.MealHttpClient
import meal.domain.MealGateway
import meal.domain.MealInteractor

class Activity_B_Meal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_meal)
        Log.i("WOW", "second act created")
        val receivedCategoryName = intent.getStringExtra("categoryName")
        val receivedFlag = intent.getStringExtra("flag")
        setSupportActionBar(findViewById(R.id.appBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = when (receivedFlag) {
            "category" -> "Category: $receivedCategoryName"
            "area" -> "$receivedCategoryName cuisine"
            else -> ""
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_meal)

        //Data Layer
        val mealHttpClient = MealHttpClient(receivedCategoryName, receivedFlag)
        //Domain Layer
        val gateway = MealGatewayImplementation(mealHttpClient)
        val interactor = MealInteractor(gateway)

        lifecycleScope.launch {
            val data = interactor.fetchData()
            recyclerView.adapter = MealAdapter(data.meal, this@Activity_B_Meal)
        }

    }

}