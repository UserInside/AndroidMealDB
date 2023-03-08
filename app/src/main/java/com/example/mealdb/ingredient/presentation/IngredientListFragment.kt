package com.example.mealdb.ingredient.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.R
import com.example.mealdb.ingredient.data.IngredientGatewayImplementation
import com.example.mealdb.ingredient.data.IngredientHttpClient
import com.example.mealdb.ingredient.domain.IngredientAdapter
import com.example.mealdb.ingredient.domain.IngredientInteractor
import kotlinx.coroutines.launch

class IngredientListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_ingredientlist, container, false)


        //data
        val client = IngredientHttpClient()
        //domain
        val gateway = IngredientGatewayImplementation(client)
        val interactor = IngredientInteractor(gateway)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_ingredient)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launch {
            val data = interactor.fetchData()
            val adapter = IngredientAdapter(data.ingredientList, requireActivity())
            recyclerView.adapter = adapter
        }

        return view
    }
}