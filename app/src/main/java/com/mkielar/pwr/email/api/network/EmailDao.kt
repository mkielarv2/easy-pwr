package com.mkielar.pwr.email.api.network

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mkielar.pwr.email.inbox.model.Email

@Dao
interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmailList(emails: List<Email>)

    @Update
    fun update(email: Email)

    @Query("SELECT * FROM Email ORDER BY id DESC")
    fun getEmails(): LiveData<List<Email>>
}