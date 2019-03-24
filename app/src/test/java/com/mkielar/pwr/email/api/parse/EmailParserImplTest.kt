package com.mkielar.pwr.email.api.parse

import com.mkielar.pwr.email.inbox.model.Email
import org.junit.Assert.assertEquals
import org.junit.Test


class EmailParserImplTest {
    private val emailParser: EmailParser = EmailParserImpl()

    @Test
    fun testSingleEmail() {
        val testStr = "while(1);\n" +
                "[0,'',102,0,1,[['NonJunk',1024]],\n" +
                "[[164,15900,1553256723,0,'Dział Spraw Międzynarodowych','Newsletter Działu Spraw Międzynarodowych',[]]],\n" +
                "94,5172224,164,164]"

        val parsed = emailParser.parse(testStr)

        assertEquals(
            listOf(
                Email(
                    164,
                    15900,
                    1553256723,
                    "Dział Spraw Międzynarodowych",
                    "Newsletter Działu Spraw Międzynarodowych"
                )
            ),
            parsed
        )
    }

    @Test
    fun testMultipleEmails() {
        val testStr = "while(1);\n" +
                "[0,'',102,0,3,[['NonJunk',1024]],\n" +
                "[[164,15900,1553256723,0,'Dział Spraw Międzynarodowych','Newsletter Działu Spraw Międzynarodowych',[]],\n" +
                "[163,28317,1553097335,1,'admin_jsos@pwr.edu.pl','Konkurs EBEC 2019',[]],\n" +
                "[162,2292,1553095837,1,'edukacja@pwr.edu.pl','[Edukacja.CL] powiadomienie o otrzymaniu nowego komunikatu',[]]],\n" +
                "94,5172224,164,164]\n"

        val parsed = emailParser.parse(testStr)

        assertEquals(
            listOf(
                Email(
                    164,
                    15900,
                    1553256723,
                    "Dział Spraw Międzynarodowych",
                    "Newsletter Działu Spraw Międzynarodowych"
                ),
                Email(
                    163,
                    28317,
                    1553097335,
                    "admin_jsos@pwr.edu.pl",
                    "Konkurs EBEC 2019"
                ),
                Email(
                    162,
                    2292,
                    1553095837,
                    "edukacja@pwr.edu.pl",
                    "[Edukacja.CL] powiadomienie o otrzymaniu nowego komunikatu"
                )
            ), parsed
        )
    }
}