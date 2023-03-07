package com.example.mealdb.receipt.presentation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealdb.R
import com.example.mealdb.meal.presentation.Activity_B_Meal
import com.example.mealdb.receipt.domain.TagsAdapter
import kotlinx.coroutines.launch
import receipt.data.ReceiptGatewayImplementation
import receipt.data.ReceiptHttpClient
import receipt.domain.ReceiptInteractor


class Activity_C_Recipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_recipe)
        setSupportActionBar(findViewById(R.id.appBar))

//        val initialActivityData: Uri? = intent?.data

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
            country.setOnClickListener {
                val mealActivity = Intent(this@Activity_C_Recipe, Activity_B_Meal::class.java)
                mealActivity.putExtra("categoryName", data?.strArea)
                mealActivity.putExtra("flag", "area")
                startActivity(mealActivity)
            }

            //set video button
            val videoButton = findViewById<Button>(R.id.videoButton)
            if (data?.strYoutube != "" && data?.strYoutube != null) {
                videoButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(data.strYoutube)
                    startActivity(intent)
                }
            } else {
                videoButton.visibility = View.GONE
            }


            //set Tags
            val recyclerViewTags = findViewById<RecyclerView>(R.id.recycler_tags)
            if (data?.strTags != null) {
                recyclerViewTags.adapter = TagsAdapter(data.tags)
            } else {
//                recyclerViewTags.layoutParams = LinearLayout.LayoutParams(0, 0)
                recyclerViewTags.visibility = View.GONE

            }

            val ingredients = data?.ingredients
            var textN = ""
            var textM = ""

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.shareButton) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "test share button")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "testim subject")
            startActivity(Intent.createChooser(shareIntent, ""))

        }
        return super.onOptionsItemSelected(item)
    }


}


// TODO добавить диплинк для sharebutton