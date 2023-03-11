package com.example.mealdb.recipe.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealdb.R
import com.example.mealdb.recipe.domain.TagsAdapter
import kotlinx.coroutines.launch


class RecipeActivity : AppCompatActivity() {
    private lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_recipe)
        setSupportActionBar(findViewById(R.id.appBar))

        val mealName = intent.getStringExtra("mealName")
        val mealId = intent.getStringExtra("mealId")
        val mealThumbnail = intent.getStringExtra("mealThumbnail")
        supportActionBar?.title = mealName


        viewModel = ViewModelProvider(
            this,
            RecipeViewModelFactory(this, mealId)
        ).get(RecipeViewModel::class.java)

        lifecycleScope.launch {

            val data = viewModel.getRecipeItem()
            //todo: почему не создает объект если вместе функции указать receiptItem ?? ведь при создании модели должна инициироваться таже гетРецИтем ..

            val image = findViewById<ImageView>(R.id.imageMealInRecipe)
            Glide
                .with(this@RecipeActivity)
                .load(viewModel.foodImage)
                .into(image)

            image.setOnClickListener{
                viewModel.openImage()
            }

            val prepare = findViewById<TextView>(R.id.prepare)
            prepare.text = "${data?.strInstructions}"

            val country = findViewById<TextView>(R.id.country)
            country.text = data?.strArea
            country.setOnClickListener {
                viewModel.openRecipeByArea()
            }

            //set video button
            val videoButton = findViewById<Button>(R.id.videoButton)
            if (data?.strYoutube != "" && data?.strYoutube != null) {
                videoButton.setOnClickListener {
                    viewModel.showVideo()
                }
            } else {
                videoButton.visibility = View.GONE
            }

            //set Tags
            val recyclerViewTags = findViewById<RecyclerView>(R.id.recycler_tags)
            if (data?.strTags != null) {
                recyclerViewTags.adapter = TagsAdapter(data.tags)
            } else {
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