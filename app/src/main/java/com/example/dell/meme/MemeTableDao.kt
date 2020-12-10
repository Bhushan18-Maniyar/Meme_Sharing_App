package com.example.dell.meme

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemeTableDao {

    /**
     *suspend is Coroutine in kotlin which makes function to run in background thread
     *
     *and we can only call this function using background thread OR by another suspend function
     *
     * basically purpose of making function suspend is to run in background bcz IO functions are heavy to run on MAIN THREAD
    and app will going to lag all the time
     *
     */

    @Insert
    suspend fun insert(memeTable: MemeTable)

    @Delete
    suspend fun delete(memeTable: MemeTable)

    @Query("Select * from meme_table order by id DESC")
    fun getAllMeme(): LiveData<List<MemeTable>>
    //    LiveData is basically life cycle aware i.e. Whenever data updated activity knows
}