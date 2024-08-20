package com.nourawesomeapps.mealmaestro.db.mealdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nourawesomeapps.mealmaestro.model.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM meal_table")
    fun getAllMeals() : LiveData<List<Meal>>

}