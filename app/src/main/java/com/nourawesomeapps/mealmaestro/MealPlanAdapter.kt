package com.nourawesomeapps.mealmaestro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.databinding.MealPlanItemLayoutBinding
import com.nourawesomeapps.mealmaestro.model.DailyMealPlan

class MealPlanAdapter : RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder>() {

    lateinit var onMealPlanClick: ((DailyMealPlan) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlanViewHolder {
        return MealPlanViewHolder(MealPlanItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: MealPlanViewHolder, position: Int) {
        val dailyMealPlan: DailyMealPlan = differ.currentList.get(position)
        holder.binding.mealPlanDayText.text = dailyMealPlan.dayOfWeek
        Glide.with(holder.itemView)
            .load(dailyMealPlan.meal.strMealThumb)
            .into(holder.binding.mealPlanImg)
        holder.binding.mealPlanNameTxt.text = dailyMealPlan.meal.strMeal
        holder.binding.deleteMealPlanBtn.setOnClickListener {
            onMealPlanClick.invoke(dailyMealPlan)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class MealPlanViewHolder(val binding: MealPlanItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<DailyMealPlan>() {
        override fun areItemsTheSame(oldItem: DailyMealPlan, newItem: DailyMealPlan): Boolean {
            return oldItem.meal.idMeal == newItem.meal.idMeal
        }

        override fun areContentsTheSame(oldItem: DailyMealPlan, newItem: DailyMealPlan): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer<DailyMealPlan>(this, diffUtil)


}