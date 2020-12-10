package com.example.dell.meme

import androidx.lifecycle.LiveData

class MemeRepository(private val memeTableDao: MemeTableDao) {

    val allMeme: LiveData<List<MemeTable>> = memeTableDao.getAllMeme()

    suspend fun insert(memeTable: MemeTable){
        memeTableDao.insert(memeTable)
    }

    suspend fun delete(memeTable: MemeTable){
        memeTableDao.delete(memeTable)
    }

}