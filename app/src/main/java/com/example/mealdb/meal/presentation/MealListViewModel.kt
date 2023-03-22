package com.example.mealdb.meal.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealdb.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import meal.data.MealGatewayImplementation
import meal.data.MealHttpClient
import meal.domain.MealListEntity
import meal.domain.MealListInteractor

class MealListViewModel(
    val context: Context,
    val categoryName: String?,
    val flag: String?,
) : ViewModel() {

    var interactor : MealListInteractor? = null

    private val _stateFlow = MutableStateFlow(MealListUiState())
    val stateFlow : StateFlow<MealListUiState> = _stateFlow.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        if (stateFlow.value.contentState == ContentState.Loading) return

        _stateFlow.update{ state -> state.copy(contentState = ContentState.Loading)}

        try {
            viewModelScope.launch {
                val mealListEntity = getMealListEntity()
                _stateFlow.update { state ->
                    state.copy(
                        mealListEntity = mealListEntity,
                        contentState = ContentState.Done
                    ) }
            }

        } catch (e: Throwable) {
            _stateFlow.update { state ->
                state.copy(contentState = ContentState.Error)
            }
        }
    }

    suspend fun getMealListEntity(): MealListEntity {
        val mealHttpClient = MealHttpClient(categoryName, flag)
        val gateway = MealGatewayImplementation(mealHttpClient)
        interactor = MealListInteractor(gateway)
        val request = interactor?.fetchData()
        val mealList = request?.mealList
        return MealListEntity(mealList)
    }

    fun getMealListEntitySortedAscendingByName() : MealListEntity? {
        return interactor?.sortByName(_stateFlow.value.mealListEntity)
    }

    fun getMealListEntitySortedDescendingByName() : MealListEntity? {
        return interactor?.sortDescendingByName(_stateFlow.value.mealListEntity)
    }

    fun getMealListSearched(query: String?) : MealListEntity? {
        return interactor?.filterMealList(query ?: "")
    }
}