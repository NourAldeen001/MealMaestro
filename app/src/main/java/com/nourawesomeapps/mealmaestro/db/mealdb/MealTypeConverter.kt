package com.nourawesomeapps.mealmaestro.db.mealdb

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {

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