package com.nourawesomeapps.mealmaestro.network

import com.nourawesomeapps.mealmaestro.model.AreaList
import com.nourawesomeapps.mealmaestro.model.CategoryList
import com.nourawesomeapps.mealmaestro.model.MealList
import com.nourawesomeapps.mealmaestro.model.MealsByCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") mealId: String) : Call<MealList>

    @GET("filter.php")
    fun getPopularMeals(@Query("c") categoryName: String) : Call<MealsByCategory>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String) : Call<MealsByCategory>

    @GET("list.php")
    fun getCountries(@Query("a") typeList: String) : Call<AreaList>

    @GET("filter.php")
    fun getMealsByArea(@Query("a") areaName: String) : Call<MealsByCategory>

    @GET("filter.php")
    fun getMealsByIngredient(@Query("i") ingredient: String) : Call<MealsByCategory>

}