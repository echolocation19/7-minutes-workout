package com.example.sevenminutesworkout.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        binding.flStart.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_exerciseFragment)
        }

        binding.flBMI.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bmiFragment)
        }

        binding.flHistory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}