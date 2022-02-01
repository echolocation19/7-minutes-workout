package com.example.sevenminutesworkout.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.data.viewmodels.HistoryViewModel
import com.example.sevenminutesworkout.databinding.FragmentFinishBinding

class FinishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null
    val binding: FragmentFinishBinding
        get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinishBinding.inflate(inflater)
        viewModel.addDateToDatabase()
        binding.btnFinish.setOnClickListener {
            findNavController().navigate(R.id.action_finishFragment_to_homeFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}