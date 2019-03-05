package com.mkielar.pwr.email.viewModel

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mkielar.pwr.email.model.Email

@Dao
interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmailList(emails: List<Email>)

    @Update
    fun update(email: Email)

    @Query("SELECT * FROM Email")
    fun getEmails(): LiveData<List<Email>>
}