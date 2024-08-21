package com.nourawesomeapps.mealmaestro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nourawesomeapps.mealmaestro.databinding.FragmentFavoritesBinding
import com.nourawesomeapps.mealmaestro.main.MainActivity
import com.nourawesomeapps.mealmaestro.main.adapter.MealAdapter
import com.nourawesomeapps.mealmaestro.main.home.HomeFragment
import com.nourawesomeapps.mealmaestro.main.viewmodel.HomeViewModel
import com.nourawesomeapps.mealmaestro.meal.MealActivity
import com.nourawesomeapps.mealmaestro.model.Meal


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoriteMealsAdapter: MealAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).homeViewModel
        favoriteMealsAdapter = MealAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavoriteMeals()
        prepareRecyclerView()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = favoriteMealsAdapter.differ.currentList.get(position)
                viewModel.deleteMeal(meal.idMeal)
                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(meal)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favoritesRecycler)

        onFavoriteMealClick()
    }

    private fun prepareRecyclerView() {
        binding.favoritesRecycler.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoriteMealsAdapter
        }
    }

    private fun observeFavoriteMeals() {
        viewModel.observeFavoriteMealsLiveData()?.observe(viewLifecycleOwner, Observer { mealList ->
            favoriteMealsAdapter.differ.submitList(mealList)
        })
    }

    private fun onFavoriteMealClick() {
        favoriteMealsAdapter.onFavoriteItemClick = { meal ->
            sendToMealActivity(meal)
        }
    }

    private fun sendToMealActivity(meal: Meal) {
        val intent = Intent(activity, MealActivity::class.java)
        intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
        intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
        intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
        startActivity(intent)
    }
}