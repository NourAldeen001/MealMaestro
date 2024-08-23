package com.nourawesomeapps.mealmaestro.db.mealplandb

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nourawesomeapps.mealmaestro.model.Meal

@TypeConverters
class MealPlanConverter {

    @TypeConverter
    fun fromMeal(meal: Meal): String {
        return Gson().toJson(meal)
    }

    @TypeConverter
    fun toMeal(mealString: String): Meal {
        val type = object : TypeToken<Meal>() {}.type
        return Gson().fromJson(mealString, type)
    }

    @TypeConverter
    fun convertFromAnyToString(attribute: Any?) : String {
        if (attribute == null)
            return ""
        return attribute as String
    }

    @TypeConverter
    fun convertFromStringToAny(attribute: String?) : Any {
        if (attribute == null)
            return ""
        return attribute
    }
}