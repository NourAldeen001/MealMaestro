package com.nourawesomeapps.mealmaestro.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nourawesomeapps.mealmaestro.model.Meal

@Dao
interface MealDao {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM meal_table")
    fun getAllMeals() : LiveData<List<Meal>>

}