package com.example.mealdb.country.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mealdb.R
import com.example.mealdb.category.presentation.MainActivity
import com.example.mealdb.country.data.Country
import com.example.mealdb.country.data.CountryGatewayImplementation
import com.example.mealdb.country.data.CountryHttpClient
import com.example.mealdb.country.domain.CountryAdapter
import com.example.mealdb.country.domain.CountryInteractor
import kotlinx.coroutines.launch

class CountryListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(R.layout.fragment_countylist, container, false)
        //data
        val client = CountryHttpClient()
        val gateway = CountryGatewayImplementation(client)
        //domain
        val interactor = CountryInteractor(gateway)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_country)
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launch {
            val data = interactor.fetchData()
            val adapter = CountryAdapter(data.countryList)
            recyclerView?.adapter = adapter

        }
        return view

    }


}