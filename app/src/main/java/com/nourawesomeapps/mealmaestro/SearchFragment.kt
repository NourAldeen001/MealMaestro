package com.nourawesomeapps.mealmaestro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.nourawesomeapps.mealmaestro.databinding.FragmentSearchBinding
import com.nourawesomeapps.mealmaestro.main.MainActivity
import com.nourawesomeapps.mealmaestro.main.adapter.MealAdapter
import com.nourawesomeapps.mealmaestro.main.home.HomeFragment
import com.nourawesomeapps.mealmaestro.main.viewmodel.HomeViewModel
import com.nourawesomeapps.mealmaestro.meal.MealActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).homeViewModel
        searchMealsAdapter = CategoryMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        setUpSearchSpinner()

        var searchJob: Job? = null
        binding.searchBarEd.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                val position = binding.searchSpinner.selectedItemPosition
                searchMeals(searchQuery.toString(), position)
                observeSearchMeals(position)
            }
        }

        onSearchResultItemClick()


        binding.goBackBtn.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

    }

    private fun setUpRecyclerView() {
        binding.searchResultRecycler.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchMealsAdapter
        }
    }

    private fun setUpSearchSpinner() {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.filter_options,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.searchSpinner.adapter = adapter
            }
        }
        binding.searchSpinner.setSelection(0)
    }

    private fun onSearchResultItemClick() {
        searchMealsAdapter.onCategoryMealItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_ID, it.idMeal)
                putExtra(HomeFragment.MEAL_NAME, it.strMeal)
                putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)
            }
            startActivity(intent)
        }
    }

    private fun searchMeals(searchQuery: String, position: Int) {
        if(searchQuery.isNotEmpty()) {
            when(position) {
                0 -> viewModel.getMealsByCategory(searchQuery)
                1 -> viewModel.getMealsByArea(searchQuery)
                2 -> viewModel.getMealsByIngredient(searchQuery)
            }
        }
    }

    private fun observeSearchMeals(position: Int) {
        when(position) {
            0 -> observeMealsByCategory()
            1 -> observeMealsByArea()
            2 -> observeMealsByIngredient()
        }
    }

    private fun observeMealsByCategory() {
        viewModel.mealsByCategory.observe(viewLifecycleOwner) { meals ->
            searchMealsAdapter.differ.submitList(meals)
        }
    }

    private fun observeMealsByArea() {
        viewModel.mealsByArea.observe(viewLifecycleOwner) { meals ->
            searchMealsAdapter.differ.submitList(meals)
        }
    }

    private fun observeMealsByIngredient() {
        viewModel.mealsByIngredient.observe(viewLifecycleOwner) { meals ->
            searchMealsAdapter.differ.submitList(meals)
        }
    }

}