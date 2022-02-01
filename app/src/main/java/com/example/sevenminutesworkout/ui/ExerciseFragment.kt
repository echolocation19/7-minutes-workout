package com.example.sevenminutesworkout.ui

import android.app.Dialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.adapters.ExerciseStatusAdapter
import com.example.sevenminutesworkout.data.db.DefaultExercisesList
import com.example.sevenminutesworkout.data.models.Exercise
import com.example.sevenminutesworkout.databinding.DialogCustomBackConfirmationBinding
import com.example.sevenminutesworkout.databinding.FragmentExerciseBinding
import com.example.sevenminutesworkout.utils.Constants.EXERCISE_COUNTING
import com.example.sevenminutesworkout.utils.Constants.EXERCISE_TIME
import com.example.sevenminutesworkout.utils.Constants.REST_COUNTING
import com.example.sevenminutesworkout.utils.Constants.REST_TIME
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList


class ExerciseFragment : Fragment(), TextToSpeech.OnInitListener {

    private var _binding: FragmentExerciseBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var restTimer: CountDownTimer
    private lateinit var exerciseTimer: CountDownTimer
    private lateinit var tts: TextToSpeech
    private lateinit var player: MediaPlayer
    private lateinit var exerciseAdapter: ExerciseStatusAdapter

    private var restProgress = 0
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<Exercise> = arrayListOf()
    private var currentExercisePosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseBinding.inflate(inflater)

        tts = TextToSpeech(requireContext(), this)

        exerciseList = DefaultExercisesList.defaultExerciseList()
        setRestView()
        setExerciseStatusRv()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customDialogForBackButton()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
    }

    fun customDialogForBackButton() {
        val customDialog = Dialog(requireContext())
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            findNavController().navigateUp()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setExerciseStatusRv() {
        binding.rvExerciseStatus.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList)
        binding.rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun setRestView() {
        binding.progressBar.progress = restProgress
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvExercise.visibility = View.INVISIBLE
        binding.flExerciseView.visibility = View.INVISIBLE
        binding.flRestView.visibility = View.VISIBLE
        binding.tvExercise.visibility = View.INVISIBLE
        binding.ivImage.visibility = View.INVISIBLE

        binding.tvUpcomingExerciseText.visibility = View.VISIBLE
        binding.tvUpcomingExercise.visibility = View.VISIBLE
        binding.tvUpcomingExercise.text = if (currentExercisePosition != exerciseList.size - 1)
            exerciseList[currentExercisePosition + 1].name else exerciseList[exerciseList.size - 1].name

        speakOut("Upcoming exercise ${exerciseList[currentExercisePosition + 1].name}")

        if (::restTimer.isInitialized) {
            restTimer.cancel()
            restProgress = 0
        }

        restTimer = object : CountDownTimer(REST_TIME, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding.progressBar.progress = REST_COUNTING - restProgress
                binding.tvTimer.text = (REST_COUNTING - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList[currentExercisePosition].isSelected = true
                exerciseAdapter.notifyDataSetChanged()
                setExerciseView()
            }
        }.start()

    }

    private fun setExerciseView() {
        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.sevenminutesworkout/" +
                        R.raw.press_start
            )
            player = MediaPlayer.create(requireActivity(), soundURI)
            player.isLooping = false
            player.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.tvTitle.visibility = View.INVISIBLE
        binding.tvExercise.visibility = View.VISIBLE
        binding.flRestView.visibility = View.INVISIBLE
        binding.flExerciseView.visibility = View.VISIBLE
        binding.tvExercise.visibility = View.VISIBLE
        binding.ivImage.visibility = View.VISIBLE
        binding.tvUpcomingExerciseText.visibility = View.INVISIBLE
        binding.tvUpcomingExercise.visibility = View.INVISIBLE

        binding.ivImage.setImageResource(exerciseList[currentExercisePosition].img)
        binding.tvExercise.text = exerciseList[currentExercisePosition].name

        if (::exerciseTimer.isInitialized) {
            exerciseTimer.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList[currentExercisePosition].name)

        exerciseTimer = object : CountDownTimer(EXERCISE_TIME, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding.pbExercise.progress = EXERCISE_COUNTING - exerciseProgress
                binding.tvExerciseTime.text = (EXERCISE_COUNTING - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList.size - 1) {
                    exerciseList[currentExercisePosition].isSelected = false
                    exerciseList[currentExercisePosition].isCompleted = true
                    exerciseAdapter.notifyDataSetChanged()
                    setRestView()
                } else {
                    findNavController().navigate(R.id.action_exerciseFragment_to_finishFragment)
                }
            }
        }.start()
    }


    /** Implementing TextToSpeech Listener */
    override fun onInit(status: Int) {
        when (status) {
            TextToSpeech.SUCCESS -> tts.language = Locale.US
            TextToSpeech.LANG_MISSING_DATA -> Snackbar.make(
                requireView(),
                "Selected language not supported!",
                Snackbar.LENGTH_SHORT
            ).show()
            TextToSpeech.LANG_NOT_SUPPORTED -> Snackbar.make(
                requireView(),
                "Selected language not supported!",
                Snackbar.LENGTH_SHORT
            ).show()
            else -> Snackbar.make(
                requireView(),
                "Selected language not supported!",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun speakOut(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        if (::restTimer.isInitialized) {
            restTimer.cancel()
            restProgress = 0
        }
        if (::exerciseTimer.isInitialized) {
            exerciseTimer.cancel()
            exerciseProgress = 0
        }
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        if (::player.isInitialized)
            player.stop()
    }

}