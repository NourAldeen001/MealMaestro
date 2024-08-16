package com.nourawesomeapps.mealmaestro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nourawesomeapps.mealmaestro.databinding.FragmentCountriesBinding
import com.nourawesomeapps.mealmaestro.main.MainActivity
import com.nourawesomeapps.mealmaestro.main.adapter.CountriesAdapter
import com.nourawesomeapps.mealmaestro.main.viewmodel.HomeViewModel

class CountriesFragment : Fragment() {

    private lateinit var binding: FragmentCountriesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var countriesAdapter: CountriesAdapter

    companion object {
        const val AREA_NAME = "com.nourawesomeapps.mealmaestro.area"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).homeViewModel
        countriesAdapter = CountriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCountriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareCountriesRecyclerView()
        viewModel.getCountries()
        observeCountriesLiveData()
        onAreaItemClick()
    }

    private fun prepareCountriesRecyclerView() {
        binding.countriesRecycler.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = countriesAdapter
        }
    }

    private fun observeCountriesLiveData() {
        viewModel.observeCountriesLiveData().observe(viewLifecycleOwner, Observer {
            countriesAdapter.differ.submitList(it)
        })
    }

    private fun onAreaItemClick() {
        countriesAdapter.onAreaItemClickListener = { area ->
            val intent = Intent(activity, AreaMealsActivity::class.java)
            intent.putExtra(AREA_NAME, area.strArea)
            startActivity(intent)
        }
    }
}