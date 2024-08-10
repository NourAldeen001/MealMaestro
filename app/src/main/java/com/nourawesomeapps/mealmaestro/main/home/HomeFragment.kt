package com.nourawesomeapps.mealmaestro.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.meal.MealActivity
import com.nourawesomeapps.mealmaestro.databinding.FragmentHomeBinding
import com.nourawesomeapps.mealmaestro.main.MainActivity
import com.nourawesomeapps.mealmaestro.main.home.viewmodel.HomeViewModel
import com.nourawesomeapps.mealmaestro.model.Meal


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
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
            sendToMealActivity(randomMeal)
        }
    }

    private fun sendToMealActivity(meal: Meal) {
        val intent = Intent(activity, MealActivity::class.java)
        intent.putExtra(MEAL_ID, meal.idMeal)
        intent.putExtra(MEAL_NAME, meal.strMeal)
        intent.putExtra(MEAL_THUMB, meal.strMealThumb)
        startActivity(intent)
    }


}