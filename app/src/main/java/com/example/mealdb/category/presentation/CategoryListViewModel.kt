package com.example.mealdb.category.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import category.data.CategoryHttpClient
import category.data.CategoryListGatewayImplementation
import category.domain.CategoryListEntity
import category.domain.CategoryListGateway
import category.domain.CategoryListInteractor
import com.example.mealdb.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryListViewModel : ViewModel() {
    lateinit var interactor: CategoryListInteractor

    private val _stateFlow = MutableStateFlow<CategoryListUiState>(CategoryListUiState())
    val stateFlow: StateFlow<CategoryListUiState> = _stateFlow.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        if (_stateFlow.value.contentState == ContentState.Loading) return

        _stateFlow.update { it.copy(contentState = ContentState.Loading) }
        try {
            viewModelScope.launch {
                val categoryListEntity = getCategoryEntity()
                _stateFlow.update { state ->
                    state.copy(
                        categoryListEntity = categoryListEntity,
                        contentState = ContentState.Done
                    )
                }
            }
        } catch (e: Throwable) {
            _stateFlow.update { it.copy(contentState = ContentState.Error) }
        }
    }


    suspend fun getCategoryEntity(): CategoryListEntity {
        val categoryHttpClient = CategoryHttpClient()
        val gateway: CategoryListGateway = CategoryListGatewayImplementation(categoryHttpClient)
        interactor = CategoryListInteractor(gateway)
        val data = interactor.fetchData()
        return data
    }

    fun getCategoryListSortedAscendingByName() : CategoryListEntity {
        return interactor.sortByName(_stateFlow.value.categoryListEntity)
    }

    fun getCategoryListSortedDescendingByName() : CategoryListEntity {
        return interactor.sortDescendingByName(_stateFlow.value.categoryListEntity)
    }

    fun getFilteredCategoryList(query: String?) : CategoryListEntity {
        return interactor.filterCategoryList(query)
    }


}