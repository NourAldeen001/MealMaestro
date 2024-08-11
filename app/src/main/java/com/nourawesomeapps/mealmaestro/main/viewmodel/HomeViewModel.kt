package com.nourawesomeapps.mealmaestro.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nourawesomeapps.mealmaestro.db.MealDatabase
import com.nourawesomeapps.mealmaestro.model.CategoryMeal
import com.nourawesomeapps.mealmaestro.model.Meal
import com.nourawesomeapps.mealmaestro.model.MealList
import com.nourawesomeapps.mealmaestro.model.MealsByCategory
import com.nourawesomeapps.mealmaestro.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealsLiveData = MutableLiveData<List<CategoryMeal>>()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
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

    fun getPopularMeals() {
        RetrofitInstance.api.getPopularMeals("Seafood").enqueue(object : Callback<MealsByCategory> {
            override fun onResponse(call: Call<MealsByCategory>, response: Response<MealsByCategory>) {
                response.body()?.let {
                    popularMealsLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategory>, t: Throwable) {
                Log.d("HomeViewModel : Popular Meals", t.message.toString())
            }
        })
    }

    fun getMealById(mealId: String) {
        RetrofitInstance.api.getMealDetails(mealId).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                response.body()?.let {
                    bottomSheetMealLiveData.postValue(it.meals.first())
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel : Get Meal By ID", t.message.toString())
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

    fun observePopularMealsLiveData() : LiveData<List<CategoryMeal>> = popularMealsLiveData

    fun observeBottomSheetMealLiveData() : LiveData<Meal> = bottomSheetMealLiveData

    fun observeFavoriteMealsLiveData() : LiveData<List<Meal>> = favoriteMealsLiveData

}