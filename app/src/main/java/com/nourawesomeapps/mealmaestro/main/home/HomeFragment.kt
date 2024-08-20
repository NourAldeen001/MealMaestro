package com.nourawesomeapps.mealmaestro.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.CategoryMealsActivity
import com.nourawesomeapps.mealmaestro.MealBottomSheetFragment
import com.nourawesomeapps.mealmaestro.R
import com.nourawesomeapps.mealmaestro.meal.MealActivity
import com.nourawesomeapps.mealmaestro.databinding.FragmentHomeBinding
import com.nourawesomeapps.mealmaestro.main.MainActivity
import com.nourawesomeapps.mealmaestro.main.adapter.CategoriesAdapter
import com.nourawesomeapps.mealmaestro.main.adapter.MostPopularMealsAdapter
import com.nourawesomeapps.mealmaestro.main.viewmodel.HomeViewModel
import com.nourawesomeapps.mealmaestro.model.Meal


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mostPopularMealsAdapter: MostPopularMealsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var randomMeal: Meal

    companion object {
        const val MEAL_ID = "com.nourawesomeapps.mealmaestro.idMeal"
        const val MEAL_NAME = "com.nourawesomeapps.mealmaestro.nameMeal"
        const val MEAL_THUMB = "com.nourawesomeapps.mealmaestro.thumbMeal"
        const val CATEGORY_NAME = "com.nourawesomeapps.mealmaestro.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = (activity as MainActivity).homeViewModel
        mostPopularMealsAdapter = MostPopularMealsAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getRandomMeal()
        observeRandomMealLiveData()
        onRandomMealClick()

        homeViewModel.getPopularMeals()
        preparePopularRecyclerView()
        observePopularMealsLiveData()
        onPopularMealItemClick()
        onPopularMealItemLongClick()

        homeViewModel.getCategories()
        prepareCategoriesRecyclerView()
        observeCategoriesLiveData()
        onCategoryItemClick()

        onSearchIconClick()
    }

    private fun observeRandomMealLiveData() {
        homeViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this@HomeFragment)
                .load(meal.strMealThumb)
                .into(binding.randomMealImg)
            randomMeal = meal
        })
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularRecyclerView() {
        binding.popularMealsRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mostPopularMealsAdapter
        }
    }

    private fun observePopularMealsLiveData() {
        homeViewModel.observePopularMealsLiveData().observe(viewLifecycleOwner, Observer { meals ->
            mostPopularMealsAdapter.differ.submitList(meals)
        })
    }

    private fun onPopularMealItemClick() {
        mostPopularMealsAdapter.onPopularMealItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, it.idMeal)
            intent.putExtra(MEAL_NAME, it.strMeal)
            intent.putExtra(MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun onPopularMealItemLongClick() {
        mostPopularMealsAdapter.onPopularMealItemLongClick = {
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(mealId = it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun observeCategoriesLiveData() {
        homeViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer {
            categoriesAdapter.differ.submitList(it)
        })
    }

    private fun prepareCategoriesRecyclerView() {
        binding.categoriesRecycler.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onCategoryItemClick() {
        categoriesAdapter.onCategoryItemClick = {
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }
    }

    private fun onSearchIconClick() {
        binding.homeSearchImg.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

}