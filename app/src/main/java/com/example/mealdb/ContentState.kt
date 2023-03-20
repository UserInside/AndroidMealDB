package com.example.mealdb

sealed class ContentState {
    object Error : ContentState()
    object Loading : ContentState()
    object Done : ContentState()
    object Idle : ContentState()
}