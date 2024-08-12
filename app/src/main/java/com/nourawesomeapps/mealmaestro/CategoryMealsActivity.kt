package com.nourawesomeapps.mealmaestro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nourawesomeapps.mealmaestro.databinding.ActivityCategoryMealsBinding
import com.nourawesomeapps.mealmaestro.main.home.HomeFragment

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryMealsViewModel = ViewModelProvider(this).get(CategoryMealsViewModel::class.java)
        categoryMealsAdapter = CategoryMealsAdapter()

        intent.getStringExtra(HomeFragment.CATEGORY_NAME)
            ?.let { categoryMealsViewModel.getMealsByCategory(it) }

        prepareMealsRecyclerView()
        observeMealsByCategoryLiveData()


        onBackClick()

    }

    private fun observeMealsByCategoryLiveData() {
        categoryMealsViewModel.observeMealsByCategoryLiveData().observe(this, Observer { categoryMeals ->
            binding.mealsOnCategoryCountTv.text = String.format("${categoryMeals.size} Items")
            categoryMealsAdapter.differ.submitList(categoryMeals)
        })
    }

    private fun prepareMealsRecyclerView() {
        binding.mealsRecycler.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }

    private fun onBackClick() {
        binding.goBackBtn.setOnClickListener {
            finish()
        }
    }

}