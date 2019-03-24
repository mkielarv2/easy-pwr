package com.mkielar.pwr.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mkielar.pwr.jsos.api.network.JsosEmail

@Dao
interface JsosEmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmailList(emails: List<JsosEmail>)

    @Update
    fun update(email: JsosEmail)

    @Query("SELECT * FROM JsosEmail")
    fun getEmails(): LiveData<List<JsosEmail>>
}