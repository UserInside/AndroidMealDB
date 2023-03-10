package com.example.mealdb

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.mealdb.category.presentation.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.bottom_sheet_filter_by_name, container, false)
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
//         val activity = requireActivity()

         val radioGroup : RadioGroup = view.findViewById(R.id.radio_group_filter)
         val radioButtonAscending = view.findViewById<RadioButton>(R.id.radio_button_filter_ascending)
         val radioButtonDescending = view.findViewById<RadioButton>(R.id.radio_button_filter_descending)

         radioButtonAscending.setOnClickListener {
             radioButtonAscending.isChecked = true
             activity.

         }

    }
}