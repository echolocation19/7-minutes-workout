package com.example.sevenminutesworkout.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sevenminutesworkout.adapters.HistoryAdapter
import com.example.sevenminutesworkout.data.viewmodels.HistoryViewModel
import com.example.sevenminutesworkout.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater)
        binding.rvDates.layoutManager = LinearLayoutManager(requireActivity())

        historyViewModel.allHistories.observe(viewLifecycleOwner, {
            binding.rvDates.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.GONE
            binding.rvDates.adapter = HistoryAdapter(it)
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}