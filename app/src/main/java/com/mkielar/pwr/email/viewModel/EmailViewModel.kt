package com.mkielar.pwr.email.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mkielar.pwr.email.model.Email

class EmailViewModel(emailDao: EmailDao) : ViewModel() {
    val emailLiveData: LiveData<List<Email>> = emailDao.getEmails()
}