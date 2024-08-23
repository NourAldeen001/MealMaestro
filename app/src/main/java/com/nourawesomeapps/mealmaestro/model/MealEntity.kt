package com.nourawesomeapps.mealmaestro.model

import androidx.room.Embedded
import androidx.room.Entity

@Entity("meal_table", primaryKeys = ["userId", "idMeal"])
data class MealEntity(
    val userId: String,
    @Embedded
    val meal: Meal
)
