package com.example.mealdb.recipe.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.mealdb.meal.presentation.MealActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

        Log.i("OOps ViewMode", "init func executed")

        Log.i(
            "OOps ViewMode - loaded preparation",
            "${stateFlow.value.recipeItem?.strInstructions}"
        )
        Log.i("OOps ViewMode - given mealID", "${mealId}")
    }

    fun fetchData() {
        Log.i("OOps ViewMode", "fetchData func execute")
        if (stateFlow.value.contentState == ContentState.Loading) {
            Log.i("OOps ViewMode", "state set \"loading\" and return")
            return
        }


        _stateFlow.update { state -> state.copy(contentState = ContentState.Loading) }
        Log.i("OOps ViewMode", "state set \"loading\" and go try to DONE")
        try {
            viewModelScope.launch {
                Log.i("OOps ViewMode", "")
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
                Log.i("OOps ViewModel -","${stateFlow.value.foodImage}")
            }


        } catch (e: Throwable) {
            _stateFlow.update { state ->
                state.copy(contentState = ContentState.Error)
            }
        }

    }

    suspend fun getRecipeItem(): RecipeItem? {
        Log.i("OOps ViewMode", "getRecipeItem func execute")
        val httpClient = RecipeHttpClient(mealId)
        val gateway: RecipeGateway = RecipeGatewayImplementation(httpClient)
        val interactor = RecipeInteractor(gateway)
        val request = interactor.fetchRecipe()
        val recipeItem = request.recipe?.meals?.get(0)
        Log.i("OOps ViewMode", "${recipeItem?.strArea}")
        return recipeItem
    }

    fun openImage() {
        val intent = Intent(context, FoodImageActivity::class.java)
        intent.putExtra("imageURI", stateFlow.value.foodImage)
        context.startActivity(intent)
    }

    fun openRecipeByArea() {
        val intent = Intent(context, MealActivity::class.java)
        intent.putExtra("categoryName", stateFlow.value.recipeItem?.strArea)
        intent.putExtra("flag", "area")
        context.startActivity(intent)
    }

    fun showVideo() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(stateFlow.value.recipeItem?.strYoutube)
        context.startActivity(intent)
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

