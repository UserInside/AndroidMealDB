package com.example.mealdb.country.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealdb.BottomSheetFragment
import com.example.mealdb.R
import com.example.mealdb.R.layout
import com.example.mealdb.country.domain.CountryAdapter
import kotlinx.coroutines.launch


class CountryListFragment : Fragment() {

    private lateinit var viewModel: CountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(layout.fragment_countylist, container, false)

        viewModel = ViewModelProvider(this).get(CountryViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_country)
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launch {
            val data = viewModel.getCountryEntity()
            val adapter = CountryAdapter(data.countryList, requireActivity())
            recyclerView?.adapter = adapter


            val searchView = activity?.findViewById<SearchView>(R.id.searchViewCategory)
            val searchButton =
                activity?.findViewById<ActionMenuItemView>(R.id.action_search_category)
            searchButton?.setOnClickListener {
                if (searchView != null) {
                    if (!searchView.isVisible) {
                        searchView.visibility = View.VISIBLE
                    } else {
                        adapter.setChangedCountryAdapter(data.countryList)
                        searchView.visibility = View.INVISIBLE
                    }
                }
            }

            val sortButton = activity?.findViewById<ActionMenuItemView>(R.id.action_sort_category)
            val bottomSheetFragment = BottomSheetFragment(
                callbackSortAscendingByName = {
                    adapter.setChangedCountryAdapter(viewModel.interactor.sortAscendingByName(data).countryList)
                },
                callbackSortDescendingByName = {
                    adapter.setChangedCountryAdapter(viewModel.interactor.sortDescendingByName(data).countryList)
                }
            )

            sortButton?.setOnClickListener {
                bottomSheetFragment.show(parentFragmentManager, "bottomSheetInCountryList")
            }


            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.setChangedCountryAdapter(
                        viewModel.interactor.filterCountryList(newText, data).countryList
                    )
                    return true
                }
            })
        }



        return view

    }


}