package com.example.mealdb.meal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mealdb.receipt.Activity_C_Recipe
import com.example.mealdb.R

class Activity_B_Meal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_meal)

        val btn = findViewById<Button>(R.id.button_back_to_first_activity)
        btn.setOnClickListener {
            startActivity(Intent(this, Activity_C_Recipe::class.java))
        }

    }


}