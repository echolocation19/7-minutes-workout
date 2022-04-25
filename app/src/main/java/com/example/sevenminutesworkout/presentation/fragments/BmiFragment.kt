package com.example.sevenminutesworkout.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.databinding.FragmentBmiBinding
import com.example.sevenminutesworkout.presentation.viewmodels.BmiViewModel
import com.google.android.material.snackbar.Snackbar

class BmiFragment : Fragment() {

    enum class UNITS {
        METRIC_UNITS_VIEW,
        US_UNITS_VIEW
    }

    private var _binding: FragmentBmiBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentBmiBinding == null")

    private var currentVisibleView = UNITS.METRIC_UNITS_VIEW

    private val bmiViewModel: BmiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmiBinding.inflate(inflater)

        setOnClickListener()
        setMetricUnitsView()
        setOnRadioChangeListener()
        setObservers()

        return binding.root
    }

    private fun setOnRadioChangeListener() {
        binding.rgUnits.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbMetricUnits -> setMetricUnitsView()
                R.id.rbUsUnits -> setUsUnitsView()
            }
        }
    }

    private fun setOnClickListener() {
        binding.btnCalculate.setOnClickListener {
            calculateUnits()
        }
    }

    private fun setObservers() {
        bmiViewModel.bmiDescription.observe(viewLifecycleOwner) {
            binding.tvBMIInfo.text = it
        }

        bmiViewModel.bmiValue.observe(viewLifecycleOwner) {
            binding.tvYourBMI.text = it
        }
    }

    private fun setMetricUnitsView() {
        currentVisibleView = UNITS.METRIC_UNITS_VIEW
        binding.tilMetricUnitHeight.visibility = View.VISIBLE
        binding.tilMetricUnitWeight.visibility = View.VISIBLE
        binding.tilUsUnitWeight.visibility = View.INVISIBLE
        binding.tilFeet.visibility = View.INVISIBLE
        binding.tilInch.visibility = View.INVISIBLE
        binding.llHeightUS.visibility = View.INVISIBLE

        binding.etUsWeight.text!!.clear()
        binding.etFeet.text!!.clear()
        binding.etInch.text!!.clear()

        binding.tvYourBMI.text = ""
        binding.tvBMIInfo.text = ""
    }

    private fun setUsUnitsView() {
        currentVisibleView = UNITS.US_UNITS_VIEW
        binding.tilMetricUnitHeight.visibility = View.INVISIBLE
        binding.tilMetricUnitWeight.visibility = View.INVISIBLE
        binding.tilUsUnitWeight.visibility = View.VISIBLE
        binding.tilFeet.visibility = View.VISIBLE
        binding.tilInch.visibility = View.VISIBLE
        binding.llHeightUS.visibility = View.VISIBLE

        binding.etHeight.text!!.clear()
        binding.etWeight.text!!.clear()

        binding.tvYourBMI.text = ""
        binding.tvBMIInfo.text = ""

    }

    private fun calculateUnits() {
        if (isValid()) {
            when (currentVisibleView) {
                UNITS.METRIC_UNITS_VIEW -> {
                    val height: String = binding.etHeight.text.toString()
                    val weight: String = binding.etWeight.text.toString()
                    bmiViewModel.calculateMetricBmi(height.toDouble(), weight.toDouble())
                }
                UNITS.US_UNITS_VIEW -> {
                    val weight: String = binding.etUsWeight.text.toString()
                    val feet: String = binding.etFeet.text.toString()
                    val inch: String = binding.etInch.text.toString()
                    bmiViewModel.calculateUsBmi(feet.toDouble(), inch.toDouble(), weight.toDouble())
                }
            }
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.fields_should_not_be_empty), Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun isValid(): Boolean {
        return when (currentVisibleView) {
            UNITS.METRIC_UNITS_VIEW -> {
                binding.etHeight.text!!.isNotBlank() && binding.etWeight.text!!.isNotBlank()
            }
            UNITS.US_UNITS_VIEW -> {
                binding.etFeet.text!!.isNotBlank() && binding.etInch.text!!.isNotBlank() &&
                        binding.etUsWeight.text!!.isNotBlank()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}