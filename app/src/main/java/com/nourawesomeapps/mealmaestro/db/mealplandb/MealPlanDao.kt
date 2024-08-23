package com.nourawesomeapps.mealmaestro.db.mealplandb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nourawesomeapps.mealmaestro.model.DailyMealPlan
import com.nourawesomeapps.mealmaestro.model.Meal

@Dao
interface MealPlanDao {

    @Query("SELECT * FROM meal_plan_table WHERE userId == :userId AND date >= :startDate")
    fun getMealsForCurrentWeek(userId: String, startDate: Long): LiveData<List<DailyMealPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(dailyMealPlan: DailyMealPlan)

    @Query("DELETE FROM meal_plan_table WHERE dayOfWeek = :dayOfWeek")
    suspend fun deleteMealsForDay(dayOfWeek: String)

    @Transaction
    suspend fun upsertMeal(dailyMealPlan: DailyMealPlan) {
        deleteMealsForDay(dailyMealPlan.dayOfWeek)
        insertMeal(dailyMealPlan)
    }

    @Delete
    suspend fun deleteMeal(dailyMealPlan: DailyMealPlan)
}