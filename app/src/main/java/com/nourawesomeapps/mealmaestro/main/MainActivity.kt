package com.nourawesomeapps.mealmaestro.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nourawesomeapps.mealmaestro.R
import com.nourawesomeapps.mealmaestro.databinding.ActivityMainBinding
import com.nourawesomeapps.mealmaestro.db.MealDatabase
import com.nourawesomeapps.mealmaestro.main.viewmodel.HomeViewModeFactory
import com.nourawesomeapps.mealmaestro.main.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val homeViewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelFactory = HomeViewModeFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostMainFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

    }
}