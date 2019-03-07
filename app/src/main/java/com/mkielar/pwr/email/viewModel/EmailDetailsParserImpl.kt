package com.mkielar.pwr.email.viewModel

import android.util.Log
import com.mkielar.pwr.email.model.EmailDetails
import org.mozilla.javascript.Context
import org.mozilla.javascript.ContextFactory
import org.mozilla.javascript.NativeArray

class EmailDetailsParserImpl : EmailDetailsParser {
    override fun parse(response: String): EmailDetails {
        val input = response.replace("while(1);\n", "")

        val rhino = ContextFactory().enterContext()
        rhino.languageVersion = Context.VERSION_1_8
        rhino.optimizationLevel = -1
        val scope = rhino.initStandardObjects()
        val eval = rhino.evaluateString(scope, "$input;", "parser", 1, null)
        val mainArray = eval as NativeArray
        val a1 = toKotlinList(mainArray[8])
        val a2 = toKotlinList(a1[a1.size - 1])
        val a3 = toKotlinList(toKotlinList(a1[0])[5])

        val content = a2[a2.size - 2].toString()
        val timestamp = mainArray[4].toString()

        var subject = ""
        var sender = ""
        for (a in a3) {
            if (a is NativeArray) {
                Log.e("asdaf: ", "$a")
                val arr = toKotlinList(a)
                when (arr[0]) {
                    "Subject" -> subject = arr[1].toString()
                    "Sender" -> sender = arr[1].toString()
                }
            }
        }

        return EmailDetails(timestamp, subject, sender, content)
    }

    private fun toKotlinList(obj: Any?) = (obj as NativeArray).toList()
}