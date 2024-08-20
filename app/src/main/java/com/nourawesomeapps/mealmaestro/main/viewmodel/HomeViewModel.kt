package com.nourawesomeapps.mealmaestro.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nourawesomeapps.mealmaestro.db.mealdb.MealDatabase
import com.nourawesomeapps.mealmaestro.model.Area
import com.nourawesomeapps.mealmaestro.model.AreaList
import com.nourawesomeapps.mealmaestro.model.Category
import com.nourawesomeapps.mealmaestro.model.CategoryList
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
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var countriesLiveData = MutableLiveData<List<Area>>()
    private var _mealsByCategory: MutableLiveData<List<CategoryMeal>> = MutableLiveData()
    val mealsByCategory: LiveData<List<CategoryMeal>> = _mealsByCategory
    private var _mealsByArea: MutableLiveData<List<CategoryMeal>> = MutableLiveData()
    val mealsByArea: LiveData<List<CategoryMeal>> = _mealsByArea
    private var _mealsByIngredient: MutableLiveData<List<CategoryMeal>> = MutableLiveData()
    val mealsByIngredient: LiveData<List<CategoryMeal>> = _mealsByIngredient

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

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {
                    categoriesLiveData.postValue(it.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel : Get Categories", t.message.toString())
            }
        })
    }

    fun getCountries() {
        RetrofitInstance.api.getCountries("list").enqueue(object : Callback<AreaList> {
            override fun onResponse(call: Call<AreaList>, response: Response<AreaList>) {
                response.body()?.let {
                    countriesLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<AreaList>, t: Throwable) {
                Log.d("HomeViewModel : Get Countries", t.message.toString())
            }
        })
    }

    fun getMealsByCategory(categoryName: String) {
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategory> {
            override fun onResponse(call: Call<MealsByCategory>, response: Response<MealsByCategory>) {
                response.body()?.let {
                    _mealsByCategory.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategory>, t: Throwable) {
                Log.d("HomeViewModel : Meals By Category", t.message.toString())
            }
        })
    }

    fun getMealsByArea(areaName: String) {
        RetrofitInstance.api.getMealsByArea(areaName).enqueue(object : Callback<MealsByCategory> {
            override fun onResponse(call: Call<MealsByCategory>, response: Response<MealsByCategory>) {
                response.body()?.let {
                    _mealsByArea.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategory>, t: Throwable) {
                Log.d("HomeViewModel : Meals By Area", t.message.toString())
            }
        })
    }

    fun getMealsByIngredient(ingredient: String) {
        RetrofitInstance.api.getMealsByIngredient(ingredient).enqueue(object : Callback<MealsByCategory> {
            override fun onResponse(call: Call<MealsByCategory>, response: Response<MealsByCategory>) {
                response.body()?.let {
                    _mealsByIngredient.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategory>, t: Throwable) {
                Log.d("HomeViewModel : Meals By Ingredient", t.message.toString())
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

    fun observeCategoriesLiveData() : LiveData<List<Category>> = categoriesLiveData

    fun observeFavoriteMealsLiveData() : LiveData<List<Meal>> = favoriteMealsLiveData

    fun observeCountriesLiveData() : LiveData<List<Area>> = countriesLiveData

}