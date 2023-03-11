package com.example.mealdb.recipe.presentation

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mealdb.R

class FoodImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_image)

        val imageURI = intent.getStringExtra("imageURI")

        val foodImageView: ImageView = findViewById(R.id.food_imageview)
        Glide
            .with(this)
            .load(imageURI)
            .into(foodImageView)

        val closeBtn = findViewById<ImageButton>(R.id.btn_close_image)
        closeBtn.setOnClickListener{
            finish()
        }

    }
}