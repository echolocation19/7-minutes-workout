package com.example.sevenminutesworkout.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sevenminutesworkout.databinding.FragmentHistoryBinding
import com.example.sevenminutesworkout.presentation.adapters.HistoryAdapter
import com.example.sevenminutesworkout.presentation.viewmodels.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding == null")

    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater)

        historyViewModel.allHistories.observe(viewLifecycleOwner) {
            binding.rvDates.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.GONE
            binding.rvDates.adapter = HistoryAdapter(it)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}