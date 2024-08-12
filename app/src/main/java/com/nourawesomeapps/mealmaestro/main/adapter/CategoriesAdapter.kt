package com.nourawesomeapps.mealmaestro.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.databinding.CategoryItemLayoutBinding
import com.nourawesomeapps.mealmaestro.model.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    lateinit var onCategoryItemClick: ((Category) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(CategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = differ.currentList.get(position)
        Glide.with(holder.itemView)
            .load(category.strCategoryThumb)
            .into(holder.binding.categoryImg)
        holder.binding.categoryTv.text = category.strCategory
        holder.itemView.setOnClickListener {
            onCategoryItemClick.invoke(category)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class CategoriesViewHolder(val binding: CategoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.idCategory == newItem.idCategory
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer<Category>(this, diffUtil)
}