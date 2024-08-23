package com.nourawesomeapps.mealmaestro.meal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.nourawesomeapps.mealmaestro.MealPlanViewModel
import com.nourawesomeapps.mealmaestro.MealPlanViewModelFactory
import com.nourawesomeapps.mealmaestro.R
import com.nourawesomeapps.mealmaestro.meal.viewmodel.MealViewModel
import com.nourawesomeapps.mealmaestro.databinding.ActivityMealBinding
import com.nourawesomeapps.mealmaestro.db.mealdb.MealDatabase
import com.nourawesomeapps.mealmaestro.db.mealplandb.MealPlanDatabase
import com.nourawesomeapps.mealmaestro.main.home.HomeFragment
import com.nourawesomeapps.mealmaestro.meal.viewmodel.MealViewModelFactory
import com.nourawesomeapps.mealmaestro.model.DailyMealPlan
import com.nourawesomeapps.mealmaestro.model.Meal

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var viewModel: MealViewModel
    private val mealPlanViewModel: MealPlanViewModel by lazy {
        val mealPlanDao = MealPlanDatabase.getInstance(this).MealPlanDao()
        val mealPlanViewModelFactory = MealPlanViewModelFactory(mealPlanDao)
        ViewModelProvider(this, mealPlanViewModelFactory).get(MealPlanViewModel::class)
    }
    private var mealToSave: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(context = this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        viewModel = ViewModelProvider(this@MealActivity, viewModelFactory).get(MealViewModel::class)

        getMealInfoFromIntent()
        setInfoInViews()

        viewModel.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onFavoriteClick()
        onAddClick()
        onReturnClick()
    }


    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.mealDetailImg)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white))
        binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
    }

    fun getVideoIdFromUrl(url: String): String? {
        val regex = Regex("v=([\\w-]+)")
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value
    }

    private fun observeMealDetailsLiveData() {
        viewModel.observeMealDetailsLiveData().observe(this, Observer { meal ->
            mealToSave = meal
            binding.categoryTv.text = meal.strCategory
            binding.areaTv.text = meal.strArea
            binding.instructionsStepsTv.text = meal.strInstructions
            binding.webView.settings.javaScriptEnabled = true
            val videoId = meal.strYoutube?.let { getVideoIdFromUrl(it) }
            binding.webView.loadUrl("https://www.youtube.com/embed/$videoId")
        })
    }

    private fun onFavoriteClick() {
        binding.addToFavoriteBtn.setOnClickListener {
            mealToSave?.let {
                Log.d("Meal", mealToSave.toString())
                viewModel.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onAddClick() {
        binding.addToMealPlanBtn.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            mealToSave?.let { meal ->
                userId?.let { usId ->
                    showAddMealDialog(usId, meal)
                }
            }
        }
    }

    private fun onReturnClick() {
        binding.returnBtn.setOnClickListener {
            finish()
        }
    }

    private fun showAddMealDialog(userId: String, mealToSave: Meal) {
        val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_meal_plan_dialog_layout, null)

        val daySpinner = view.findViewById<Spinner>(R.id.meal_plan_add_spinner)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daysOfWeek)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daySpinner.adapter = adapter
        builder.setView(view)

        builder.setPositiveButton("Add") { dialog, _ ->
            val selectedDay = daySpinner.selectedItem.toString()
            mealPlanViewModel.addMeal(
                DailyMealPlan(userId, selectedDay,
                    mealToSave, System.currentTimeMillis())
            )
            Toast.makeText(this, "Add ${mealToSave.strMeal} Successfully", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
