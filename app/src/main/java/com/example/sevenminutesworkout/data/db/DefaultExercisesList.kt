package com.example.sevenminutesworkout.data.db

import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.data.models.Exercise

object DefaultExercisesList {

    fun defaultExerciseList(): ArrayList<Exercise> {
        val exerciseList = ArrayList<Exercise>()

        val jumpingJacks = Exercise(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
        )
        exerciseList.add(jumpingJacks)

        val abdominalCrunch = Exercise(
            2,
            "Abdominal Crunch",
            R.drawable.ic_abdominal_crunch,
        )
        exerciseList.add(abdominalCrunch)

        val highKneesRunningInPlace = Exercise(
            3,
            "High Knees Running In Place",
            R.drawable.ic_high_knees_running_in_place,
        )
        exerciseList.add(highKneesRunningInPlace)

        val lunge = Exercise(
            4,
            "Lunge",
            R.drawable.ic_lunge,
        )
        exerciseList.add(lunge)

        val plank = Exercise(
            5,
            "Plank",
            R.drawable.ic_plank,
        )
        exerciseList.add(plank)

        val pushUp = Exercise(
            6,
            "Push Up",
            R.drawable.ic_push_up,
        )
        exerciseList.add(pushUp)

        val pushUpAndRotation = Exercise(
            7,
            "Push Up And Rotation",
            R.drawable.ic_push_up_and_rotation,
        )
        exerciseList.add(pushUpAndRotation)

        val sidePlank = Exercise(
            8,
            "Side Plank",
            R.drawable.ic_side_plank,
        )
        exerciseList.add(sidePlank)

        val squat = Exercise(
            9,
            "Squat",
            R.drawable.ic_squat,
        )
        exerciseList.add(squat)

        val stepUpOntoChair = Exercise(
            10,
            "Step Up Onto Chair",
            R.drawable.ic_step_up_onto_chair,
        )
        exerciseList.add(stepUpOntoChair)

        val tricepsDipOnChair = Exercise(
            11,
            "Triceps Dip On Chair",
            R.drawable.ic_triceps_dip_on_chair,
        )
        exerciseList.add(tricepsDipOnChair)

        val wallSit = Exercise(
            12,
            "Wall Sit",
            R.drawable.ic_wall_sit,
        )
        exerciseList.add(wallSit)

        return exerciseList
    }


}