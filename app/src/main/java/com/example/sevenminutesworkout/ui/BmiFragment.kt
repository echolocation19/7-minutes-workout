package com.example.sevenminutesworkout.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.data.viewmodels.BmiViewModel
import com.example.sevenminutesworkout.databinding.FragmentBmiBinding
import com.example.sevenminutesworkout.utils.UNITS.*
import com.google.android.material.snackbar.Snackbar

class BmiFragment : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding
        get() = _binding!!
    private var currentVisibleView = METRIC_UNITS_VIEW

    private val bmiViewModel: BmiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBmiBinding.inflate(inflater)

        binding.btnCalculate.setOnClickListener {
            calculateUnits()
        }

        setMetricUnitsView()

        binding.rgUnits.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbMetricUnits -> setMetricUnitsView()
                R.id.rbUsUnits -> setUsUnitsView()
            }
        }

        bmiViewModel.bmiDescription.observe(viewLifecycleOwner, {
            binding.tvBMIInfo.text = it
        })

        bmiViewModel.bmiValue.observe(viewLifecycleOwner, {
            binding.tvYourBMI.text = it
        })

        return binding.root
    }

    private fun setMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
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
        currentVisibleView = US_UNITS_VIEW
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
                METRIC_UNITS_VIEW -> {
                    val height: String = binding.etHeight.text.toString()
                    val weight: String = binding.etWeight.text.toString()
                    bmiViewModel.calculateMetricBmi(height.toDouble(), weight.toDouble())
                }
                US_UNITS_VIEW -> {
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
            )
                .show()
        }
    }

    private fun isValid(): Boolean {
        return when (currentVisibleView) {
            METRIC_UNITS_VIEW -> {
                binding.etHeight.text!!.isNotBlank() && binding.etWeight.text!!.isNotBlank()
            }
            US_UNITS_VIEW -> {
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