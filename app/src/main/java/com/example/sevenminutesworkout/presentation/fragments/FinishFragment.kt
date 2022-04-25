package com.example.sevenminutesworkout.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.databinding.FragmentFinishBinding
import com.example.sevenminutesworkout.presentation.viewmodels.HistoryViewModel

class FinishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null
    private val binding: FragmentFinishBinding
        get() = _binding ?: throw RuntimeException("FragmentFinishBinding == null")

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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