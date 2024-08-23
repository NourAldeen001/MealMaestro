package com.nourawesomeapps.mealmaestro

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nourawesomeapps.mealmaestro.db.mealplandb.MealPlanDao
import com.nourawesomeapps.mealmaestro.model.DailyMealPlan
import kotlinx.coroutines.launch
import java.util.Calendar

class MealPlanViewModel(private val mealPlanDao: MealPlanDao) : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    val mealsForWeek: LiveData<List<DailyMealPlan>>? = userId?.let { mealPlanDao.getMealsForCurrentWeek(it, getStartOfWeek().timeInMillis) }

    fun addMeal(dailyMealPlan: DailyMealPlan) {
        viewModelScope.launch {
            mealPlanDao.upsertMeal(dailyMealPlan)
        }
    }

    fun deleteMeal(dailyMealPlan: DailyMealPlan) {
        viewModelScope.launch {
            mealPlanDao.deleteMeal(dailyMealPlan)
        }
    }

    private fun getStartOfWeek(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return calendar
    }

}

class MealPlanViewModelFactory(private val mealPlanDao: MealPlanDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MealPlanViewModel::class.java)) {
            return MealPlanViewModel(mealPlanDao) as T
        }
        else {
            throw IllegalArgumentException()
        }
    }
}