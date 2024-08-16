package com.nourawesomeapps.mealmaestro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nourawesomeapps.mealmaestro.model.CategoryMeal
import com.nourawesomeapps.mealmaestro.model.MealsByCategory
import com.nourawesomeapps.mealmaestro.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AreaMealsViewModel : ViewModel() {

    private var areaMealsMutableLiveData = MutableLiveData<List<CategoryMeal>>()

    fun getMealsByArea(areaName: String) {
        RetrofitInstance.api.getMealsByArea(areaName).enqueue(object : Callback<MealsByCategory> {
            override fun onResponse(call: Call<MealsByCategory>, response: Response<MealsByCategory>) {
                response.body()?.let {
                    areaMealsMutableLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategory>, t: Throwable) {
                Log.d("AreaMealsViewModel : Meals By Area", t.message.toString())
            }
        })
    }

    fun observeAreaMealsLiveData() : LiveData<List<CategoryMeal>> = areaMealsMutableLiveData
}