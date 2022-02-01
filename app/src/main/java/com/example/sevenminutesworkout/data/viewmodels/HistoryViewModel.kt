package com.example.sevenminutesworkout.data.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.sevenminutesworkout.data.db.HistoryDatabase
import com.example.sevenminutesworkout.data.models.History
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val db: HistoryDatabase = HistoryDatabase.getInstance(application)
    private val historyDao = db.historyDao()

    internal var allHistories: LiveData<List<History>> = db.historyDao().fetchAllDates()

    fun addDateToDatabase() {
        val c = Calendar.getInstance()
        val dateTime = c.time

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        viewModelScope.launch {
            historyDao.insertHistory(History(date))
        }
    }

}