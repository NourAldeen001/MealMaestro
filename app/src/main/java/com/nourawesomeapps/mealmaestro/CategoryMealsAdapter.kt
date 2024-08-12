package com.nourawesomeapps.mealmaestro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.databinding.MealItemLayoutBinding
import com.nourawesomeapps.mealmaestro.model.CategoryMeal

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(MealItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        val categoryMeal = differ.currentList.get(position)
        Glide.with(holder.itemView)
            .load(categoryMeal.strMealThumb)
            .into(holder.binding.mealImg)
        holder.binding.mealTv.text = categoryMeal.strMeal
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class CategoryMealsViewHolder(val binding: MealItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<CategoryMeal>() {
        override fun areItemsTheSame(oldItem: CategoryMeal, newItem: CategoryMeal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: CategoryMeal, newItem: CategoryMeal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer<CategoryMeal>(this, diffUtil)
}