package com.mkielar.pwr.email.api.parse

import com.mkielar.pwr.email.details.model.EmailDetails

interface EmailDetailsParser {
    fun parse(response: String): EmailDetails
}