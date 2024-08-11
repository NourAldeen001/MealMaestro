package com.nourawesomeapps.mealmaestro.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.databinding.PopularItemLayoutBinding
import com.nourawesomeapps.mealmaestro.model.CategoryMeal

class MostPopularMealsAdapter : RecyclerView.Adapter<MostPopularMealsAdapter.MostPopularMealsViewHolder>() {

    lateinit var onPopularMealItemClick : ((CategoryMeal) -> Unit)
    lateinit var onPopularMealItemLongClick : ((CategoryMeal) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularMealsViewHolder {
        return MostPopularMealsViewHolder(PopularItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MostPopularMealsViewHolder, position: Int) {
        val popularMeal = differ.currentList.get(position)
        Glide.with(holder.itemView)
            .load(popularMeal.strMealThumb)
            .into(holder.binding.popularMealImg)

        holder.itemView.setOnClickListener {
            onPopularMealItemClick.invoke(popularMeal)
        }

        holder.itemView.setOnLongClickListener {
            onPopularMealItemLongClick.invoke(popularMeal)
            true
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class MostPopularMealsViewHolder(val binding: PopularItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<CategoryMeal>() {
        override fun areItemsTheSame(oldItem: CategoryMeal, newItem: CategoryMeal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: CategoryMeal, newItem: CategoryMeal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
}