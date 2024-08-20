package com.nourawesomeapps.mealmaestro.db.mealdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nourawesomeapps.mealmaestro.model.Meal

@Database(entities = [Meal::class], version = 2)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao() : MealDao

    companion object {
        @Volatile
        private var INSTANCE: MealDatabase? = null

        fun getInstance(context: Context) : MealDatabase {
            return INSTANCE ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = tempInstance
                tempInstance
            }
        }

    }

}