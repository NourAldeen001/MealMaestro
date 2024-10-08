package com.nourawesomeapps.mealmaestro.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.databinding.MealItemLayoutBinding
import com.nourawesomeapps.mealmaestro.model.Meal

class MealAdapter : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    lateinit var onFavoriteItemClick: ((Meal) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(MealItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val favoriteMeal = differ.currentList.get(position)
        Glide.with(holder.itemView)
            .load(favoriteMeal.strMealThumb)
            .into(holder.binding.mealImg)
        holder.binding.mealTv.text = favoriteMeal.strMeal
        holder.itemView.setOnClickListener {
            onFavoriteItemClick.invoke(favoriteMeal)
        }
    }

    class MealViewHolder(val binding: MealItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
}