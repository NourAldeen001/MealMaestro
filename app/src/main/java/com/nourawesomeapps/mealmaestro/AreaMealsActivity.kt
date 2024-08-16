package com.nourawesomeapps.mealmaestro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nourawesomeapps.mealmaestro.databinding.ActivityAreaMealsBinding
import com.nourawesomeapps.mealmaestro.main.home.HomeFragment
import com.nourawesomeapps.mealmaestro.meal.MealActivity

class AreaMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAreaMealsBinding
    private lateinit var viewModel: AreaMealsViewModel
    private lateinit var areaMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAreaMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AreaMealsViewModel::class.java)
        areaMealsAdapter = CategoryMealsAdapter()

        prepareMealsRecyclerView()
        intent.getStringExtra(CountriesFragment.AREA_NAME)?.let {
            viewModel.getMealsByArea(it)
        }
        observeMealsByAreaLiveData()
        onMealItemClick()

        onGoBackClick()
    }

    private fun prepareMealsRecyclerView() {
        binding.mealsRecycler.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = areaMealsAdapter
        }
    }

    private fun observeMealsByAreaLiveData() {
        viewModel.observeAreaMealsLiveData().observe(this, Observer {
            areaMealsAdapter.differ.submitList(it)
            binding.mealsOnAreaCountTv.text = String.format("${it.size} Items")
        })
    }

    private fun onMealItemClick() {
        areaMealsAdapter.onCategoryMealItemClick = {
            val intent = Intent(this, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_ID, it.idMeal)
                putExtra(HomeFragment.MEAL_NAME, it.strMeal)
                putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)
            }
            startActivity(intent)
        }
    }

    private fun onGoBackClick() {
        binding.goBackBtn.setOnClickListener {
            finish()
        }
    }
}