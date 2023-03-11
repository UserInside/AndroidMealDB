package com.example.mealdb.recipe.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.mealdb.meal.presentation.Activity_B_Meal
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

    init {
        viewModelScope.launch {
            recipeItem = getRecipeItem()
            foodImage = recipeItem?.strMealThumb
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
        val intent = Intent(context, Activity_B_Meal::class.java)
        intent.putExtra("categoryName", recipeItem?.strArea)
        intent.putExtra("flag", "area")
        context.startActivity(intent)
    }

    fun showVideo() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(recipeItem?.strYoutube)
        context.startActivity(intent)
    }


}

