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
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
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
    private lateinit var mealId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_recipe)

        setSupportActionBar(findViewById(R.id.appBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mealId = intent.data?.lastPathSegment ?: intent.getStringExtra("mealId") ?: ""

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

            supportActionBar?.title = state.recipeItem?.strMeal

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

            val shortcut = ShortcutInfoCompat.Builder(this, mealId)
                .setShortLabel("${state.recipeItem?.strMeal}")
                .setIcon(IconCompat.createWithResource(this, R.mipmap.ic_launcher)) //TODO доделать загрузку иконок каждого блюда
                .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("http://com.example.mealdb/recipe/$mealId")))
                .build()

            ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)

        }.launchIn(lifecycleScope)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu_recipe, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mealId = viewModel.stateFlow.value.recipeItem?.idMeal

        when (item.itemId) {
            R.id.shareButton -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://com.example.mealdb/recipe/$mealId")
                startActivity(Intent.createChooser(shareIntent, "wow mama chooser"))
            }
        }
        return super.onOptionsItemSelected(item)
    }


//TODO С лончмод стандарт все норм, но рецепты складываются в стек. Если поставить синглтоп, то не получается -- открываем ссылку при действующем активити,
// то оно не обновляется. Вернее обновляется, потом назад стирается интент экшн и актуальный экран запускается повторно.
// onNewIntent проходит один крут нормально, но затем сразу еще один с обнуленным интентэкшн. Надо пофиксить.
//        }
//    }


}


