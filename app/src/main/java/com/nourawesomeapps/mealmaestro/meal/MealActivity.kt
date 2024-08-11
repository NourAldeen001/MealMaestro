package com.nourawesomeapps.mealmaestro.meal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nourawesomeapps.mealmaestro.meal.viewmodel.MealViewModel
import com.nourawesomeapps.mealmaestro.databinding.ActivityMealBinding
import com.nourawesomeapps.mealmaestro.db.MealDatabase
import com.nourawesomeapps.mealmaestro.main.home.HomeFragment
import com.nourawesomeapps.mealmaestro.meal.viewmodel.MealViewModelFactory
import com.nourawesomeapps.mealmaestro.model.Meal

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var viewModel: MealViewModel
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
}
