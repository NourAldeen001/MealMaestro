package com.nourawesomeapps.mealmaestro.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plan_table", primaryKeys = ["userId", "idMeal", "dayOfWeek"])
data class DailyMealPlan(
    val userId: String,
    val dayOfWeek: String,
    @Embedded
    var meal: Meal,
    val date: Long,
)
