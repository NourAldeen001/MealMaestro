package com.nourawesomeapps.mealmaestro.network

import com.nourawesomeapps.mealmaestro.model.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") mealId: String) : Call<MealList>

}