package com.example.sevenminutesworkout.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sevenminutesworkout.R
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.properties.Delegates

class BmiViewModel(application: Application) : AndroidViewModel(application) {
    private val resources = application.resources!!

    private val _bmiValue = MutableLiveData("")
    val bmiValue: LiveData<String>
        get() = _bmiValue

    private val _bmiDescription = MutableLiveData("")
    val bmiDescription: LiveData<String>
        get() = _bmiDescription

    private var bmi by Delegates.notNull<Double>()

    fun calculateMetricBmi(height: Double, weight: Double) {
        val heightInMeters = height / 100.0
        bmi = weight / heightInMeters.pow(2.0)
        _bmiValue.value = BigDecimal(bmi).setScale(2, RoundingMode.HALF_EVEN).toString()
        setBmiResult()
    }

    fun calculateUsBmi(feet: Double, inch: Double, weight: Double) {
        val height = feet * 12 + inch
        bmi = 703 * weight / height.pow(2.0)
        _bmiValue.value = BigDecimal(bmi).setScale(2, RoundingMode.HALF_EVEN).toString()
        setBmiResult()
    }

    private fun setBmiResult() {
        _bmiDescription.value = when {
            bmi < 18.5 -> resources.getString(R.string.bmi_underweight)
            bmi in 18.5..24.9 -> resources.getString(R.string.bmi_normal)
            bmi in 25.0..29.9 ->
                resources.getString(R.string.bmi_overweight)
            bmi > 30.0 -> resources.getString(R.string.bmi_obese)
            else -> ""
        }
    }

}