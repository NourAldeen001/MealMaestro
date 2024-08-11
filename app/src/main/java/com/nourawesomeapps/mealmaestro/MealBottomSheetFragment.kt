package com.nourawesomeapps.mealmaestro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nourawesomeapps.mealmaestro.databinding.FragmentMealBottomSheetBinding
import com.nourawesomeapps.mealmaestro.main.MainActivity
import com.nourawesomeapps.mealmaestro.main.home.HomeFragment
import com.nourawesomeapps.mealmaestro.main.viewmodel.HomeViewModel
import com.nourawesomeapps.mealmaestro.meal.MealActivity


private const val MEAL_ID = "meal"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThumb: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        homeViewModel = (activity as MainActivity).homeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            homeViewModel.getMealById(it)
        }
        observeBottomSheetMealLiveData()
        onBottomDialogClick()
    }

    companion object {

        @JvmStatic
        fun newInstance(mealId: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, mealId)
                }
            }
    }

    private fun observeBottomSheetMealLiveData() {
        homeViewModel.observeBottomSheetMealLiveData().observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this@MealBottomSheetFragment)
                .load(meal.strMealThumb)
                .into(binding.bottomSheetMealImg)
            binding.bottomSheetAreaTv.text = meal.strArea
            binding.bottomSheetCategoryTv.text = meal.strCategory
            binding.bottomSheetMealNameTv.text = meal.strMeal

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    private fun onBottomDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if (mealName != null && mealThumb != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }
        }
    }

}
