package com.nourawesomeapps.mealmaestro.db.mealdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nourawesomeapps.mealmaestro.model.Meal
import com.nourawesomeapps.mealmaestro.model.MealEntity

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(mealEntity: MealEntity)

    @Query("DELETE FROM meal_table WHERE userId = :userId AND idMeal = :mealId")
    suspend fun delete(mealId: String, userId: String)

    @Query("SELECT * FROM meal_table WHERE userId == :userId")
    fun getAllMeals(userId: String) : LiveData<List<Meal>>

}