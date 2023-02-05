package com.example.mealdb.receipt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mealdb.R
import com.example.mealdb.category.MainActivity

class Activity_C_Recipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_recipe)

        val btn_back_to_categories = findViewById<Button>(R.id.btn_back_to_a)
        btn_back_to_categories.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}