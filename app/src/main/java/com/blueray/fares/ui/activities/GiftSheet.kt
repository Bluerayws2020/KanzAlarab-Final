package com.blueray.fares.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.GiftAdapter
import com.blueray.fares.databinding.BottomSheetLayoutBinding
import com.blueray.fares.model.Grid

import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class GiftSheet : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetLayoutBinding

    lateinit var adapter: GiftAdapter
    lateinit var showButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetLayoutBinding.inflate(layoutInflater)


        setUpRecyclerView()
//        setUpButton()
        return binding.root
    }
    fun setUpRecyclerView(){

//adapter = GiftAdapter(mutableListOf(
//    Grid(R.drawable.gift1 , "Greetings" , "25 coins"),
//            Grid(R.drawable.gift2 , "Boom" , "15 coins"),
//            Grid(R.drawable.gift4 , "Big love" , "45 coins"),
//            Grid(R.drawable.oh , "Surprise" , "25 coins"),
//            Grid(R.drawable.oh , "Surprise" , "25 coins"),
//            Grid(R.drawable.oh , "Surprise" , "25 coins"),
//            Grid(R.drawable.oh , "Surprise" , "25 coins"),
//            Grid(R.drawable.oh , "Surprise" , "25 coins"),
//        ))
        binding.gridRv.adapter = adapter
        binding.gridRv.layoutManager = GridLayoutManager(requireContext() , 2 , GridLayoutManager.HORIZONTAL ,false)
    }
    @SuppressLint("SuspiciousIndentation")
    fun setUpButton(){
        showButton.setOnClickListener {
            Log.d("why" , "clicked")
        val dialog = GiftSheet()
            dialog.show(parentFragmentManager , "bottom")
            dismiss()
        }
    }
}