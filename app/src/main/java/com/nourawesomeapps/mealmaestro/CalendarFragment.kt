package com.nourawesomeapps.mealmaestro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nourawesomeapps.mealmaestro.databinding.FragmentCalendarBinding
import com.nourawesomeapps.mealmaestro.db.mealplandb.MealPlanDatabase


class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var mealPlanAdapter: MealPlanAdapter
    private lateinit var mealPlanViewModel: MealPlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mealPlanDao = MealPlanDatabase.getInstance(requireContext()).MealPlanDao()
        val mealPlanViewModelFactory = MealPlanViewModelFactory(mealPlanDao)
        mealPlanViewModel = ViewModelProvider(this, mealPlanViewModelFactory).get(MealPlanViewModel::class)
        mealPlanAdapter = MealPlanAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        mealPlanViewModel.mealsForWeek?.observe(viewLifecycleOwner) { meals ->
            mealPlanAdapter.differ.submitList(meals)
        }

        onDeleteMealPlanClick()

    }

    private fun prepareRecyclerView() {
        binding.mealsPlanRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mealPlanAdapter
        }
    }

    private fun onDeleteMealPlanClick() {
        mealPlanAdapter.onMealPlanClick = { dailyMealPlan ->
            mealPlanViewModel.deleteMeal(dailyMealPlan)
        }
    }
}