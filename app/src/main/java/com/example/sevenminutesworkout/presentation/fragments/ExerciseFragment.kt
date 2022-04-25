package com.example.sevenminutesworkout.presentation.fragments

import android.app.Dialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.data.db.ExercisesList
import com.example.sevenminutesworkout.data.models.Exercise
import com.example.sevenminutesworkout.databinding.DialogCustomBackConfirmationBinding
import com.example.sevenminutesworkout.databinding.FragmentExerciseBinding
import com.example.sevenminutesworkout.presentation.adapters.ExerciseStatusAdapter
import java.util.*

class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentExerciseBinding == null")

    private lateinit var restTimer: CountDownTimer
    private lateinit var exerciseTimer: CountDownTimer
    private lateinit var tts: TextToSpeech
    private var player: MediaPlayer? = null
    private lateinit var exerciseAdapter: ExerciseStatusAdapter

    private var restProgress = 0
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<Exercise> = arrayListOf()
    private var currentExercisePosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProperties()
        setRestView()
        setExerciseStatusRv()
        setNavigationCallback()
    }

    private fun setProperties() {
        tts = TextToSpeech(activity) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            }
        }
        exerciseList = ExercisesList.defaultExerciseList()
    }

    private fun setNavigationCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customDialogForBackButton()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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

    private fun setPropertiesForRestView() {
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
    }

    private fun setRestTimer() {
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

    private fun setRestView() {
        setPropertiesForRestView()
        setRestTimer()
        speakOut("Upcoming exercise ${exerciseList[currentExercisePosition + 1].name}")
    }

    private fun setPropertiesForExerciseView() {
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
    }

    private fun setupMediaPlayer() {
        try {
            if (player == null) {
                val soundURI = Uri.parse(
                    URI_PATH + R.raw.press_start
                )
                player = MediaPlayer.create(requireActivity(), soundURI)
                player!!.isLooping = false
            }
            player!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setExerciseTimer() {
        if (::exerciseTimer.isInitialized) {
            exerciseTimer.cancel()
            exerciseProgress = 0
        }
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

    private fun setExerciseView() {
        setupMediaPlayer()
        setPropertiesForExerciseView()
        speakOut(exerciseList[currentExercisePosition].name)
        setExerciseTimer()
    }

    private fun speakOut(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
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
        player?.stop()
    }

    companion object {
        const val REST_TIME: Long = 10000
        const val EXERCISE_TIME: Long = 30000
        const val REST_COUNTING: Int = 10
        const val EXERCISE_COUNTING: Int = 30
        const val URI_PATH = "android.resource://com.example.sevenminutesworkout/"
    }


}