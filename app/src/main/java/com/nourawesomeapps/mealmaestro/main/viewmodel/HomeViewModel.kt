package com.nourawesomeapps.mealmaestro.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nourawesomeapps.mealmaestro.db.MealDatabase
import com.nourawesomeapps.mealmaestro.model.Meal
import com.nourawesomeapps.mealmaestro.model.MealList
import com.nourawesomeapps.mealmaestro.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                response.body()?.let {
                    randomMealLiveData.postValue(it.meals[0])
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel : Random Meal", t.message.toString())
            }
        })
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun observeRandomMealLiveData() : LiveData<Meal> = randomMealLiveData

    fun observeFavoriteMealsLiveData() : LiveData<List<Meal>> = favoriteMealsLiveData

}