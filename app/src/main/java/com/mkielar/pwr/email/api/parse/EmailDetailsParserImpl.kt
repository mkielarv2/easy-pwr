package com.mkielar.pwr.email.api.parse

import android.util.Log
import com.mkielar.pwr.email.details.model.EmailDetails
import org.mozilla.javascript.Context
import org.mozilla.javascript.ContextFactory
import org.mozilla.javascript.NativeArray
import org.mozilla.javascript.ScriptableObject

class EmailDetailsParserImpl : EmailDetailsParser {
    override fun parse(response: String): EmailDetails {
        val input = removeBlockingCode(response)

        val rhino = configureRhino()
        val scope = rhino.initStandardObjects()

        //CAUTION!
        //You are entering danger zone of magic parser code with spaghetti design pattern!
        //You can dive into it but I strongly recommend you to move along because this
        //code may cause severe headaches. Feel free to go to different part of this repository.
        //You have been warned, proceed at your own risk...
        val eval = evaluateJavaScript(rhino, scope, input)
        val mainArray = eval as NativeArray
        val a1 = toKotlinList(mainArray[8])
        val a2 = toKotlinList(a1[a1.size - 1])
        val a3 = toKotlinList(toKotlinList(a1[0])[5])

        val contentRaw = a2[a2.size - 2].toString()
        val content = "<html><head></head><body>$contentRaw</body></html>"
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

    private fun removeBlockingCode(response: String) = response.replace("while(1);\n", "")

    private fun configureRhino(): Context = ContextFactory().enterContext().apply {
        languageVersion = Context.VERSION_1_8
        optimizationLevel = -1
    }

    private fun evaluateJavaScript(
        rhino: Context,
        scope: ScriptableObject?,
        input: String
    ) = rhino.evaluateString(scope, "$input;", "parser", 1, null)

    private fun toKotlinList(obj: Any?) = (obj as NativeArray).toList()
}