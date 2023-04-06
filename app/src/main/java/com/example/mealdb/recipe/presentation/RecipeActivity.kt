package com.example.mealdb.recipe.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.mealdb.ContentState
import com.example.mealdb.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class RecipeActivity : AppCompatActivity() {
    private lateinit var viewModel: RecipeViewModel
    private lateinit var mealName: String
    private lateinit var mealId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)

        setContentView(R.layout.activity_c_recipe)

        if (!this::mealId.isInitialized) {
            mealId = intent.getStringExtra("mealId") ?: ""
        }
        mealName = intent.getStringExtra("mealName") ?: ""


        setSupportActionBar(findViewById(R.id.appBar))
        supportActionBar?.title = mealName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(
            this, RecipeViewModelFactory(this, mealId)
        ).get(RecipeViewModel::class.java)

        val image = findViewById<ImageView>(R.id.imageMealInRecipe)
        val prepare = findViewById<TextView>(R.id.prepare)
        val errorView = findViewById<View>(R.id.includeError)
        val progressView = findViewById<View>(R.id.includeProgressBar)
        val contentView = findViewById<View>(R.id.contentViewRecipe)
        val recyclerViewTags = findViewById<RecyclerView>(R.id.recycler_tags)
        val videoButton = findViewById<Button>(R.id.videoButton)
        val country = findViewById<TextView>(R.id.country)


//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {

        viewModel.stateFlow.onEach { state ->
            when (state.contentState) {
                ContentState.Loading,
                ContentState.Idle -> {
                    progressView?.visibility = View.VISIBLE
                    errorView?.visibility = View.GONE
                    contentView?.visibility = View.GONE
                }
                ContentState.Error -> {
                    progressView?.visibility = View.GONE
                    errorView?.visibility = View.VISIBLE
                    contentView?.visibility = View.GONE
                }
                ContentState.Done -> {
                    progressView?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                    contentView?.visibility = View.VISIBLE
                }
            }

            image?.let { img ->
                Glide.with(this@RecipeActivity)
                    .load(state.foodImage)
                    .placeholder(R.drawable.baseline_hourglass_bottom_24_black)
                    .error(R.drawable.baseline_block_24_black)
                    .fallback(R.drawable.baseline_visibility_off_24_black)
                    .into(img)
            }
            image.setOnClickListener {
                viewModel.openImage()
            }

            prepare?.text = "${state.recipeItem?.strInstructions}"

            country.text = state.recipeItem?.strArea
            country.setOnClickListener {
                viewModel.openRecipeListByArea()
            }

            videoButton.setOnClickListener {
                viewModel.showVideo()
            }

            if (state.recipeItem?.strTags != null) {
                recyclerViewTags.visibility = View.VISIBLE
                recyclerViewTags.adapter = viewModel.showTags()
            }

            val textIngredientName = findViewById<TextView>(R.id.ingredientName)
            textIngredientName.text = viewModel.getIngredientsList()
            val textIngredientMeasure = findViewById<TextView>(R.id.ingredientMeasure)
            textIngredientMeasure.text = viewModel.getMeasuresList()

        }.launchIn(lifecycleScope)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu_recipe, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mealId = viewModel.stateFlow.value.recipeItem?.idMeal
        val strMeal = viewModel.stateFlow.value.recipeItem?.strMeal

        when (item.itemId) {
            R.id.shareButton -> {

                val shareIntent = Intent(Intent.ACTION_SEND) //если URI передать отсюда, то активити сразу его ловит и открывает в себе же, обновляя страницу с рецептом.
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://com.example.mealdb/recipe/$mealId")
                startActivity(Intent.createChooser(shareIntent, "wow mama chooser"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent) {
        Log.i("onNewIntent", "1")
        handleIntent(intent)
        Log.i("onNewIntent", "2")
        super.onNewIntent(intent)
        Log.i("onNewIntent", "3")


    }

    private fun handleIntent(intent: Intent) {
        Log.i("handleIntent", "1")

        if (intent.action == Intent.ACTION_VIEW) {
            Log.i("handleIntent", "2")

            intent.setClass(this@RecipeActivity, RecipeActivity::class.java)

            mealId = intent.data?.lastPathSegment ?: ""

            intent.putExtra("mealId", mealId)
            intent.putExtra("mealName", "WOW rabotaet")
            startActivity(intent)

        }



    }
}


