package com.example.mealdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(
    private val callbackSortAscendingByName: () -> Unit,
    private val callbackSortDescendingByName: () -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_filter_by_name, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup: RadioGroup = view.findViewById(R.id.radio_group_filter)
        val radioButtonAscending =
            view.findViewById<RadioButton>(R.id.radio_button_filter_ascending)
        val radioButtonDescending =
            view.findViewById<RadioButton>(R.id.radio_button_filter_descending)

        radioButtonAscending.setOnClickListener {
            callbackSortAscendingByName()
            dismiss()
        }
        radioButtonDescending.setOnClickListener {
            callbackSortDescendingByName()
            dismiss()

        }

    }
}