package com.nourawesomeapps.mealmaestro.meal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nourawesomeapps.mealmaestro.db.mealdb.MealDatabase
import com.nourawesomeapps.mealmaestro.model.Meal
import com.nourawesomeapps.mealmaestro.model.MealList
import com.nourawesomeapps.mealmaestro.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(private val mealDatabase: MealDatabase) : ViewModel() {

    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetails(mealId: String) {
        RetrofitInstance.api.getMealDetails(mealId).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                response.body()?.let {
                    mealDetailsLiveData.postValue(it.meals.get(0))
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealViewModel : Meal Details", t.message.toString())
            }
        })
    }

    fun observeMealDetailsLiveData() : LiveData<Meal> = mealDetailsLiveData

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}