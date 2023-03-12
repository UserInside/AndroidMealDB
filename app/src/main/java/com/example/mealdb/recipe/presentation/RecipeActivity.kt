package com.example.mealdb.recipe.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mealdb.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            this, RecipeViewModelFactory(this, mealId)
        ).get(RecipeViewModel::class.java)

        val image = findViewById<ImageView>(R.id.imageMealInRecipe)
        val prepare = findViewById<TextView>(R.id.prepare)
        val contentView = findViewById<View>(R.id.contentView)
        val loadingView = findViewById<View>(R.id.loadingView)
        val errorView = findViewById<View>(R.id.errorView)
        val buttonRetry = findViewById<View>(R.id.buttonRetry)

        buttonRetry.setOnClickListener {
            viewModel.fetchData()
        }

        lifecycleScope.launch {
            delay(3000)
            Log.d("WOW", "1")
//            viewModel.stateFlow.collect {
//                Log.d("WOW", "2")
//                Glide.with(this@RecipeActivity).load(it?.strMealThumb)
//                    .placeholder(R.drawable.baseline_hourglass_bottom_24_black)
//                    .error(R.drawable.baseline_block_24_black)
//                    .fallback(R.drawable.baseline_visibility_off_24_black).into(image)
//
//                prepare.text = "${it?.strInstructions}"
//                Log.d("WOW", "${it?.strInstructions}")
//            }
        }

        viewModel.stateFlow
            .onEach { state ->
                when (state.contentState) {
                    ContentState.Idle,
                    ContentState.Loading -> {
                        loadingView?.visibility = View.VISIBLE
                        errorView?.visibility = View.GONE
                        contentView?.visibility = View.GONE
                    }
                    ContentState.Done -> {
                        loadingView?.visibility = View.GONE
                        errorView?.visibility = View.GONE
                        contentView?.visibility = View.VISIBLE
                    }
                    ContentState.Error -> {
                        loadingView?.visibility = View.GONE
                        errorView?.visibility = View.VISIBLE
                        contentView?.visibility = View.GONE
                    }
                }
                image?.let { img ->
                    Glide.with(this@RecipeActivity)
                        .load(state.recipeItem?.strMealThumb)
                        .placeholder(R.drawable.baseline_hourglass_bottom_24_black)
                        .error(R.drawable.baseline_block_24_black)
                        .fallback(R.drawable.baseline_visibility_off_24_black)
                        .into(img)
                }
                prepare?.text = "${state.recipeItem?.strInstructions}"
            }
            .launchIn(lifecycleScope)

//            val image = findViewById<ImageView>(R.id.imageMealInRecipe)
//            Glide
//                .with(this@RecipeActivity)
//                .load(viewModel.foodImage)
//                .placeholder(R.drawable.baseline_hourglass_bottom_24_black)
//                .error(R.drawable.baseline_block_24_black)
//                .fallback(R.drawable.baseline_visibility_off_24_black)
//                .into(image)

//            image.setOnClickListener{
//                viewModel.openImage()
//            }
//
//            val prepare = findViewById<TextView>(R.id.prepare)
//            prepare.text = "${data?.strInstructions}"

        //set Tags
//            val recyclerViewTags = findViewById<RecyclerView>(R.id.recycler_tags)
//            if (data?.strTags != null) {
//                recyclerViewTags.adapter = TagsAdapter(data?.tags)
//            } else {
//                recyclerViewTags.visibility = View.GONE
//            }
//
//            val ingredients = data?.ingredients
//            var textN = ""
//            var textM = ""
//
//            for (i in ingredients!!.size - 1 downTo 0) {
//                textN = "${ingredients[i].strIngredient}\n" + textN
//                textM = "${ingredients[i].strMeasure}\n" + textM
//
//            }
//
//            val textIngredientName = findViewById<TextView>(R.id.ingredientName)
//            textIngredientName.text = textN
//            val textIngredientMeasure = findViewById<TextView>(R.id.ingredientMeasure)
//            textIngredientMeasure.text = textM

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