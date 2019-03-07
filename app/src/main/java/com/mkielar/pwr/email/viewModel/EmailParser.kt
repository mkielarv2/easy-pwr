package com.mkielar.pwr.email.viewModel

import com.mkielar.pwr.email.model.Email

interface EmailParser {
    fun parse(response: String): List<Email>
}