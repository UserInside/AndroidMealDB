package com.example.mealdb.recipe.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealdb.meal.presentation.MealActivity
import kotlinx.coroutines.launch
import receipt.data.RecipeGatewayImplementation
import receipt.data.RecipeHttpClient
import receipt.data.RecipeItem
import receipt.domain.RecipeInteractor

class RecipeViewModel(
    private val context: Context,
    private val mealId: String?,
) : ViewModel() {

    var recipeItem: RecipeItem? = null
    var foodImage: String? = null
    var foodImagePreview: String? = null

    init {
        viewModelScope.launch {
            recipeItem = getRecipeItem()
            foodImage = recipeItem?.strMealThumb
            foodImagePreview = "$foodImage/preview"
        }
    }

    suspend fun getRecipeItem(): RecipeItem? {
        val httpClient = RecipeHttpClient(mealId)
        val gateway = RecipeGatewayImplementation(httpClient)
        val interactor = RecipeInteractor(gateway)
        val request = interactor.fetchRecipe()
        val recipeItem = request.receipt?.meals?.get(0)
        return recipeItem
    }

    fun openImage() {
        val intent = Intent(context, FoodImageActivity::class.java)
        intent.putExtra("imageURI", foodImage)
        context.startActivity(intent)
    }

    fun openRecipeByArea() {
        val intent = Intent(context, MealActivity::class.java)
        intent.putExtra("categoryName", recipeItem?.strArea)
        intent.putExtra("flag", "area")
        context.startActivity(intent)
    }

    fun showVideo() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(recipeItem?.strYoutube)
        context.startActivity(intent)
    }


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

