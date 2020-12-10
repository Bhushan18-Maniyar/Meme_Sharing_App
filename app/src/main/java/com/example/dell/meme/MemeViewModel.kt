package com.example.dell.meme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MemeRepository
    val allMeme: LiveData<List<MemeTable>>

    init {
        val dao = MemeDatabase.getDatabase(application).getMemeTableDao()
        allMeme = dao.getAllMeme()
        repository = MemeRepository(dao)
    }

    fun deleteMeme(memeTable: MemeTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(memeTable)
    }

    fun insert(memeTable: MemeTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(memeTable)
    }
}