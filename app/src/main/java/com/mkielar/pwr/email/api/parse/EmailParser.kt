package com.mkielar.pwr.email.api.parse

import com.mkielar.pwr.email.inbox.model.Email

interface EmailParser {
    fun parse(response: String): List<Email>
}