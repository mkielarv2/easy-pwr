package com.mkielar.pwr.email.viewModel.parse

import com.mkielar.pwr.email.model.EmailDetails

interface EmailDetailsParser {
    fun parse(response: String): EmailDetails
}