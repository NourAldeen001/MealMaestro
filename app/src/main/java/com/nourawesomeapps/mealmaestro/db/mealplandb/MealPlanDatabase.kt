package com.nourawesomeapps.mealmaestro.db.mealplandb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nourawesomeapps.mealmaestro.model.DailyMealPlan

@Database(entities = [DailyMealPlan::class], version = 3)
@TypeConverters(MealPlanConverter::class)
abstract class MealPlanDatabase : RoomDatabase() {
    abstract fun MealPlanDao(): MealPlanDao

    companion object {
        @Volatile
        private var INSTANCE: MealPlanDatabase? = null

        fun getInstance(context: Context) : MealPlanDatabase {
            return INSTANCE ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MealPlanDatabase::class.java,
                    "meal_plan_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = tempInstance
                tempInstance
            }
        }
    }
}