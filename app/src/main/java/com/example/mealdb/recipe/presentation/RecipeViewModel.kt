package com.example.mealdb.recipe.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import receipt.data.RecipeGatewayImplementation
import receipt.data.RecipeHttpClient
import receipt.data.RecipeItem
import receipt.domain.RecipeInteractor

class RecipeViewModel(
    private val context: Context,
    private val mealId: String?,
) : ViewModel() {
    private var _stateFlow = MutableStateFlow(RecipeUiState())
    val stateFlow: StateFlow<RecipeUiState> = _stateFlow

    init {
        fetchData()
    }

    fun fetchData() {
        if (stateFlow.value.contentState == ContentState.Loading) return
        try {
            _stateFlow.update { state ->
                state.copy(
                    contentState = ContentState.Loading,
                )
            }
            viewModelScope.launch {
                val item = getRecipeItem()
                _stateFlow.update { state ->
                    val foodImage = item?.strMealThumb
                    state.copy(
                        recipeItem = item,
                        foodImage = foodImage,
                        foodImagePreview = foodImage?.let { "$foodImage/preview" },
                        contentState = ContentState.Done,
                    )
                }
            }
        } catch (throwable: Throwable) {
            _stateFlow.update { state ->
                state.copy(
                    contentState = ContentState.Error,
                )
            }
        }
    }

    suspend fun getRecipeItem(): RecipeItem? {
        Log.d("WOW", "4")
        val httpClient = RecipeHttpClient(mealId)
        val gateway = RecipeGatewayImplementation(httpClient)
        val interactor = RecipeInteractor(gateway)
        val request = interactor.fetchRecipe()
        val recipeItem = request.receipt?.meals?.get(0)
        return recipeItem
    }

//    fun openImage() {
//        val intent = Intent(context, FoodImageActivity::class.java)
//        intent.putExtra("imageURI", foodImage)
//        context.startActivity(intent)
//    }
//
//    fun openRecipeByArea() {
//        val intent = Intent(context, MealActivity::class.java)
//        intent.putExtra("categoryName", recipeItem?.strArea)
//        intent.putExtra("flag", "area")
//        context.startActivity(intent)
//    }
//
//    fun showVideo() {
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse(recipeItem?.strYoutube)
//        context.startActivity(intent)
//    }


//    fun getIngredientsList() : String {
//        val ingredients = recipeItem?.ingredients
//        var textN = ""
//        for (i in ingredients!!.size - 1 downTo 0) {
//            textN = "${ingredients[i].strIngredient}\n" + textN
//
//        }
//        return textN
//    }
//    fun getMeasuresList() : String {
//        val ingredients = recipeItem?.ingredients
//        var textM = ""
//        for (i in ingredients!!.size - 1 downTo 0) {
//            textM = "${ingredients[i].strMeasure}\n" + textM
//        }
//        return textM
//    }


}

