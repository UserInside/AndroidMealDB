package com.example.mealdb.category

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() { //раньше было ComponentActivity()
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
//        recyclerView.adapter = CategoryAdapter(getCatList())

        lifecycleScope.launch {
            val data = interactor.fetchData()
            recyclerView.adapter = CategoryAdapter(data.categoryList)
        }


//            val secondActivity = Intent(this, Activity_B_Meal::class.java)
//            secondActivity.putExtra("textFromMainActivity", editText.text.toString())
//            startActivity(secondActivity)
    }

    private fun getCatList(): List<String> {
        return this.resources.getStringArray(R.array.cat_names).toList()
    }

//    fun request() : String {
//
//        val text = resources.openRawResource(R.raw.json_meal_raw)
//            .bufferedReader().use { it.readText() }
//
//        return text
//
//
//    }

}
