package com.example.mealdb.recipe.presentation

import android.content.Intent
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
                    Log.i("OOps Activity", "state - loading")
                }
                ContentState.Error -> {
                    progressView?.visibility = View.GONE
                    errorView?.visibility = View.VISIBLE
                    contentView?.visibility = View.GONE
                    Log.i("OOps Activity", "state - error")
                }
                ContentState.Done -> {
                    progressView?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                    contentView?.visibility = View.VISIBLE
                    Log.i("OOps Activity", "state - done")
                }
            }

            image?.let { img ->
                Log.i("OOps Activity", "loading image by URI")
                Glide.with(this@RecipeActivity)
                    .load(state.foodImage)
                    .placeholder(R.drawable.baseline_hourglass_bottom_24_black)
                    .error(R.drawable.baseline_block_24_black)
                    .fallback(R.drawable.baseline_visibility_off_24_black)
                    .into(img)
            }
            image.setOnClickListener{
                viewModel.openImage()
            }

            prepare?.text = "${state.recipeItem?.strInstructions}"

            country.text = state.recipeItem?.strArea
            country.setOnClickListener{
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