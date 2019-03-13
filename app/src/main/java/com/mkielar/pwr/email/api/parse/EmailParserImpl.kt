package com.mkielar.pwr.email.api.parse

import com.mkielar.pwr.email.inbox.model.Email
import java.util.regex.Pattern

class EmailParserImpl : EmailParser {
    override fun parse(response: String): List<Email> {
        val emails = mutableListOf<Email>()

        val pattern = Pattern.compile("\\[\\d+,\\d+,\\d+,\\d+,'.[^']+','.[^']+',\\[]]")
        val matcher = pattern.matcher(response)
        while (matcher.find()) {
            val raw = matcher.group()

            val elements = splitUp(raw)

            //todo handle status (elements[3])
            emails.add(
                Email(
                    elements[0].toInt(),
                    elements[1].toLong(),
                    elements[2].toLong(),
                    removeQuotes(elements[4]),
                    removeQuotes(elements[5]).trim()
                )
            )
        }
        return emails
    }

    private fun splitUp(raw: String): MutableList<String> {
        val elements = mutableListOf<String>()
        var safeMode = false
        var last = 1
        for (i in 1 until raw.length) {
            if (raw[i] == '\'') {
                safeMode = !safeMode
                continue
            }
            if (raw[i] != (if (safeMode) '\'' else ',')) continue
            else {
                if (safeMode) safeMode = !safeMode
                elements.add(raw.substring(last, i))
                last = i + 1
            }
        }
        return elements
    }

    private fun removeQuotes(input: String): String = input.removePrefix("'").removeSuffix("'")
}