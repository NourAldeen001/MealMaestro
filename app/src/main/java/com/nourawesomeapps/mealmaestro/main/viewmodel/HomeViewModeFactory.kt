package com.nourawesomeapps.mealmaestro.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nourawesomeapps.mealmaestro.db.MealDatabase

class HomeViewModeFactory(
    private val mealDatabase: MealDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDatabase) as T
    }
}