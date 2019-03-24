package com.mkielar.pwr.email.api.parse

import com.mkielar.pwr.email.details.model.EmailDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class EmailDetailsParserImplTest {
    private val emailDetailsParser: EmailDetailsParser = EmailDetailsParserImpl()

    @Test
    fun testSimple() {
        val testStr = "while(1);\n" +
                "[0,'','INBOX',150,1551744015,1551744014,2274,1,\n" +
                "[[null,'text/html',262,true,[],\n" +
                "[['Return-path','<edukacja@pwr.edu.pl>'],\n" +
                "['Received','from z-mta4.wcss.wroc.pl (z-mta3.wcss.wroc.pl [156.17.193.134]) by z-ms1.wcss.wroc.pl (Oracle Communications Messaging Server 7.0.5.36.0 64bit (built O\\\n" +
                "ct 19 2015)) with ESMTP id <0PNV00CU79CF4H70@z-ms1.wcss.wroc.pl> for 123456@student.pwr.edu.pl; Tue, 05 Mar 2019 01:00:15 +0100 (CET)'],\n" +
                "['Original-recipient','rfc822;123456@student.pwr.edu.pl'],\n" +
                "['MIME-version','1.0'],\n" +
                "['Content-type','text/html; charset=UTF-8'],\n" +
                "['Received','from oas-51 ([156.17.28.246]) by z-mta4.wcss.wroc.pl (Oracle Communications Messaging Server 8.0.1.1.0 64bit (built Jun 15 2016)) with ESMTPPA id <0PNV\\\n" +
                "00AO59CE8560@z-mta4.wcss.wroc.pl> for 123456@student.pwr.edu.pl (ORCPT 123456@student.pwr.edu.pl); Tue, 05 Mar 2019 01:00:15 +0100 (CET)'],\n" +
                "['X-PMX-Spam','Gauge=XII, Probability=12%, Report=\\' CTYPE_JUST_HTML 0.848, HTML_NO_HTTP 0.1, MIME_LOWER_CASE 0.05, BODYTEXTH_SIZE_10000_LESS 0, BODY_SIZE_1000_LESS 0,\\\n" +
                " BODY_SIZE_2000_LESS 0, BODY_SIZE_200_299 0, BODY_SIZE_5000_LESS 0, BODY_SIZE_7000_LESS 0, DATE_MISSING 0, INVALID_MSGID_NO_FQDN 0, NO_CTA_URI_FOUND 0,\\\n" +
                " NO_REAL_NAME 0, NO_URI_FOUND 0, NO_URI_HTTPS 0, RDNS_NXDOMAIN 0, RDNS_SUSP 0, RDNS_SUSP_GENERIC 0, SMALL_BODY 0, __CT 0, __CTE 0, __CTYPE_HTML 0, __CT\\\n" +
                "YPE_IS_HTML 0, __HAS_FROM 0, __HAS_HTML 0, __HAS_MSGID 0, __MIME_HTML 0, __MIME_HTML_ONLY 0, __MIME_TEXT_H 0, __MIME_TEXT_H1 0, __MIME_VERSION 0, __SAN\\\n" +
                "E_MSGID 0, __SUBJ_ALPHA_END 0, __TAG_EXISTS_HTML 0, __TO_MALFORMED_2 0, __TO_NO_NAME 0\\''],\n" +
                "['X-PMX-Version','6.4.5.2775670, Antispam-Engine: 2.7.2.2107409, Antispam-Data: 2019.3.4.235116, AntiVirus-Engine: 5.58.0, AntiVirus-Data: 2019.3.4.5580002'],\n" +
                "['Date-warning','Date header was inserted by z-mta4.wcss.wroc.pl'],\n" +
                "['Date','Tue, 05 Mar 2019 01:00:14 +0100 (CET)'],\n" +
                "['Sender','edukacja@pwr.edu.pl'],\n" +
                "['Message-id','<3211069.1581744014736.JavaMail.eduapp@oas-51>'],\n" +
                "['From','edukacja@pwr.edu.pl'],\n" +
                "['To','123456@student.pwr.edu.pl'],\n" +
                "['Subject','[Edukacja.CL] powiadomienie o otrzymaniu nowego komunikatu'],\n" +
                "['Content-transfer-encoding','quoted-printable']],\n" +
                "'<span><p><table><tr><td><p>\\r\\nWitaj! W systemie Politechniki Wrocławskiej pojawił się nowy komunikat. Temat:   \\'listy\\'  W celu odczytania treści, pr\\\n" +
                "oszę zalogować się do systemu i wybrać opcję Wiadomości.\\r\\n</td></tr></table></'+'span>\\r\\n',true]]]\n"

        val actual = emailDetailsParser.parse(testStr)

        assertEquals(
            EmailDetails(
                "1.551744015E9",
                "[Edukacja.CL] powiadomienie o otrzymaniu nowego komunikatu",
                "edukacja@pwr.edu.pl",
                "<html><head></head><body><span><p><table><tr><td><p>\r\n" +
                        "Witaj! W systemie Politechniki Wrocławskiej pojawił się nowy komunikat. Temat:   'listy'  W celu odczytania treści, proszę zalogować się do systemu i wybrać opcję Wiadomości.\r\n" +
                        "</td></tr></table></span>\r\n" +
                        "</body></html>"
            ),
            actual
        )
    }
}