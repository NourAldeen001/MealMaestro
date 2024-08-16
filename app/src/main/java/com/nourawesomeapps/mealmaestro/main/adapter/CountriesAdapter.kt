package com.nourawesomeapps.mealmaestro.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.R
import com.nourawesomeapps.mealmaestro.databinding.CountryItemLayoutBinding
import com.nourawesomeapps.mealmaestro.model.Area

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    lateinit var onAreaItemClickListener: ((Area) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        return CountriesViewHolder(CountryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val area = differ.currentList.get(position)
        Glide.with(holder.itemView)
            .load(R.drawable.area_back)
            .into(holder.binding.countryImg)
        holder.binding.countryTv.text = area.strArea
        holder.itemView.setOnClickListener {
            onAreaItemClickListener.invoke(area)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class CountriesViewHolder(val binding: CountryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Area>() {
        override fun areItemsTheSame(oldItem: Area, newItem: Area): Boolean {
            return oldItem.strArea == newItem.strArea
        }

        override fun areContentsTheSame(oldItem: Area, newItem: Area): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer<Area>(this, diffUtil)
}