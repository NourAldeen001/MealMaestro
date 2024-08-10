package com.nourawesomeapps.mealmaestro.meal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nourawesomeapps.mealmaestro.db.MealDatabase

class MealViewModelFactory(
    private val mealDatabase: MealDatabase
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealDatabase) as T
    }
}