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

class CategoryMealsViewModel : ViewModel() {

    private var mealsByCategoryLiveData = MutableLiveData<List<CategoryMeal>>()

    fun getMealsByCategory(categoryName: String) {
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategory> {
            override fun onResponse(call: Call<MealsByCategory>, response: Response<MealsByCategory>) {
                response.body()?.let {
                    mealsByCategoryLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategory>, t: Throwable) {
                Log.d("CategoryMealsViewModel : Meals By Category", t.message.toString())
            }
        })
    }

    fun observeMealsByCategoryLiveData() : LiveData<List<CategoryMeal>> = mealsByCategoryLiveData
}