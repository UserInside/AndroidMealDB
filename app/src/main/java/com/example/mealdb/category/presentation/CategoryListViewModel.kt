package com.example.mealdb.category.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import category.domain.CategoryListEntity
import category.domain.CategoryListInteractor
import com.example.mealdb.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//TODO
// Независимо от того, используете ли вы DI контейнеры или нет,
// внедрение через конструктор (Constructor Injection) должен быть
// первым способом управления зависимостями. Его использование не
// только позволит сделать отношения между классами более явными,
// но также позволит определить проблемы с дизайном, когда
// количество параметров конструктора превысит определенную
// границу. К тому же, все современные контейнеры внедрения зависимостей поддерживают данный паттерн.
// https://habr.com/ru/articles/352530/
class CategoryListViewModel(
    private val interactor: CategoryListInteractor
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(CategoryListUiState())
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
        return interactor.fetchData()
    }

    fun getCategoryListSortedAscendingByName(): CategoryListEntity {
        return interactor.sortByName(_stateFlow.value.categoryListEntity)
    }

    fun getCategoryListSortedDescendingByName(): CategoryListEntity {
        return interactor.sortDescendingByName(_stateFlow.value.categoryListEntity)
    }

    fun getFilteredCategoryList(query: String?): CategoryListEntity {
        return interactor.filterCategoryList(query)
    }

    companion object {
        fun factory(
            interactor: CategoryListInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { CategoryListViewModel(interactor) }
        }
    }
}