package com.example.mealdb.recipe.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealdb.ContentState
import com.example.mealdb.meal.presentation.MealListActivity
import com.example.mealdb.recipe.domain.TagsAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import receipt.data.Ingredient
import receipt.data.RecipeGatewayImplementation
import receipt.data.RecipeHttpClient
import receipt.data.RecipeItem
import receipt.domain.RecipeGateway
import receipt.domain.RecipeInteractor

class RecipeViewModel(
    private val context: Context,
    private val mealId: String?,
) : ViewModel() {

    private var _stateFlow = MutableStateFlow(RecipeUiState())
    val stateFlow : StateFlow<RecipeUiState> = _stateFlow

    init {
        fetchData()
    }

    fun fetchData() {
        if (stateFlow.value.contentState == ContentState.Loading) return

        _stateFlow.update { state -> state.copy(contentState = ContentState.Loading) }
        try {
            viewModelScope.launch {
                val recipeItem = getRecipeItem()
                _stateFlow.update { state ->
                    val foodImage = recipeItem?.strMealThumb
                    state.copy(
                        recipeItem = recipeItem,
                        foodImage = foodImage,
                        foodImagePreview = foodImage?.let { "$foodImage/preview" },
                        contentState = ContentState.Done
                    )
                }
            }

        } catch (e: Throwable) {
            _stateFlow.update { state ->
                state.copy(contentState = ContentState.Error)
            }
        }
    }

    suspend fun getRecipeItem(): RecipeItem? {
        val httpClient = RecipeHttpClient(mealId)
        val gateway: RecipeGateway = RecipeGatewayImplementation(httpClient)
        val interactor = RecipeInteractor(gateway)
        val request = interactor.fetchRecipe()
        val recipeItem = request.recipe?.meals?.get(0)
        return recipeItem
    }

    fun openImage() {
        val intent = Intent(context, FoodImageActivity::class.java)
        intent.putExtra("imageURI", stateFlow.value.foodImage)
        context.startActivity(intent)
    }

    fun openRecipeListByArea() {
        val intent = Intent(context, MealListActivity::class.java)
        intent.putExtra("categoryName", stateFlow.value.recipeItem?.strArea)
        intent.putExtra("flag", "area")
        context.startActivity(intent)
    }

    fun showVideo() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(stateFlow.value.recipeItem?.strYoutube)
        context.startActivity(intent)
    }

    fun showTags() : TagsAdapter {
        return TagsAdapter(stateFlow.value.recipeItem?.tags)
    }


    fun getIngredientsList() : String? {
        val ingredients : List<Ingredient>? = stateFlow.value.recipeItem?.ingredients
        var textN = ""
        for (i in (ingredients?.size?.minus(1) ?: 0)  downTo 0) {
            textN = "${ingredients?.get(i)?.strIngredient}\n" + textN
            Log.i("OOPs", "ingredient - $i ")
        }
        return textN
    }

    fun getMeasuresList() : String {
        val ingredients : List<Ingredient>? = stateFlow.value.recipeItem?.ingredients
        var textM = ""
        for (i in (ingredients?.size?.minus(1) ?: 0) downTo 0) {
            textM = "${ingredients?.get(i)?.strMeasure}\n" + textM
        }
        return textM
    }


}

