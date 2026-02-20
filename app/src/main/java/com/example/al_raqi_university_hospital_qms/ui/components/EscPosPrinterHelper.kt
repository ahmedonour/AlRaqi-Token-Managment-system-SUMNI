package com.example.al_raqi_university_hospital_qms.ui.components

import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import java.lang.Exception

class EscPosPrinterHelper {

    /**
     * Prints a sample using a generic ESC/POS library via TCP (Internal for many Sunmi devices)
     * Sunmi K2 often uses internal loopback 127.0.0.1 for its printer service if accessed via TCP.
     */
    fun printSample(text: String) {
        Thread {
            try {
                // Many internal printers on Android kiosks listen on 127.0.0.1:9100
                val connection = TcpConnection("127.0.0.1", 9100, 1500)
                val printer = EscPosPrinter(connection, 203, 80f, 42)
                
                val formattedText = "[L]\n" +
                        "[C]<u><font size='big'>$text</font></u>\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[C]${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}\n" +
                        "[L]\n" +
                        "[L]\n" +
                        "[L]\n" +
                        "[C]<cut/>"
                
                printer.printFormattedTextAndCut(formattedText)
                connection.disconnect()
                Log.d("PrinterHelper", "Print successful")
            } catch (e: Exception) {
                Log.e("PrinterHelper", "Print failed: ${e.message}")
                e.printStackTrace()
            }
        }.start()
    }
}
