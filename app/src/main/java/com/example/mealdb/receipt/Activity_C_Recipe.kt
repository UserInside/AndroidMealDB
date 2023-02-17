package com.example.mealdb.receipt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerInside
import com.example.mealdb.R
import com.example.mealdb.category.MainActivity
import com.example.mealdb.receipt.domain.TagsAdapter
import kotlinx.coroutines.launch
import receipt.data.ReceiptGatewayImplementation
import receipt.data.ReceiptHttpClient
import receipt.data.ReceiptItem
import receipt.domain.ReceiptInteractor

class Activity_C_Recipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_recipe)
        setSupportActionBar(findViewById(R.id.appBar))

        val mealName = intent.getStringExtra("mealName")
        val mealId = intent.getStringExtra("mealId")
        val mealThumbnail = intent.getStringExtra("mealThumbnail")
        supportActionBar?.title = mealName

        //Data Layer
        val httpClient = ReceiptHttpClient(mealId)
        //Domain
        val gateway = ReceiptGatewayImplementation(httpClient)
        val interactor = ReceiptInteractor(gateway)


        lifecycleScope.launch {
            val request = interactor.fetchReceipt()
            val data = request.receipt?.meals?.get(0)
            Log.i("WOW", "data $data ++++ ")

            val image = findViewById<ImageView>(R.id.imageMealInRecipe)
            Glide
                .with(this@Activity_C_Recipe)
                .load(data?.strMealThumb) //todo ура! надо добавить индекс и тогда появятся стр поля!! ура
                // брать индекс 0 видится некрасивым, но работает только так.... если упбрать список из рецепта класса , то жсон не читается правильно
                .into(image)

            val prepare = findViewById<TextView>(R.id.prepare)
            prepare.text = "${data?.strInstructions}"

            val country = findViewById<TextView>(R.id.country)
            country.text = data?.strArea

            val recyclerViewTags = findViewById<RecyclerView>(R.id.recycler_tags)
            if (data?.strTags != null) {
                recyclerViewTags.adapter = TagsAdapter(data.tags)
            } else {
                recyclerViewTags.layoutParams = LinearLayout.LayoutParams(0, 0)

            }

            val ingredients = data?.ingredients
            var textN  = ""
            var textM  = ""

            for (i in ingredients!!.size - 1 downTo 0) {
                textN = "${ingredients[i].strIngredient}\n" + textN
                textM = "${ingredients[i].strMeasure}\n" + textM

            }

            val textIngredientName = findViewById<TextView>(R.id.ingredientName)
            textIngredientName.text = textN
            val textIngredientMeasure = findViewById<TextView>(R.id.ingredientMeasure)
            textIngredientMeasure.text = textM
        }
    }
}