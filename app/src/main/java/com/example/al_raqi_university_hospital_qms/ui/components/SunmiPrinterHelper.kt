package com.example.al_raqi_university_hospital_qms.ui.components

import android.content.Context
import android.util.Log
import com.sunmi.printerx.PrinterSdk
import com.sunmi.printerx.style.BaseStyle

class SunmiPrinterHelper {
    private var printer: PrinterSdk.Printer? = null

    fun initPrinter(context: Context, onStatusUpdate: (Boolean) -> Unit = {}) {
        try {
            PrinterSdk.getInstance().getPrinter(context, object : PrinterSdk.PrinterListen {
                override fun onDefPrinter(printer: PrinterSdk.Printer) {
                    this@SunmiPrinterHelper.printer = printer
                    onStatusUpdate(true)
                    Log.d("SunmiPrinterX", "Default printer connected")
                }

                override fun onPrinters(printers: MutableList<PrinterSdk.Printer>) {
                    Log.d("SunmiPrinterX", "Printers found: ${printers.size}")
                    if (this@SunmiPrinterHelper.printer == null && printers.isNotEmpty()) {
                        this@SunmiPrinterHelper.printer = printers[0]
                        onStatusUpdate(true)
                    }
                }
            })
        } catch (e: Exception) {
            Log.e("SunmiPrinterX", "Init error", e)
            onStatusUpdate(false)
        }
    }

    fun printToken(text: String) {
        val p = printer ?: run {
            Log.e("SunmiPrinterX", "Printer not initialized")
            return
        }
        try {
            val lineApi = p.lineApi()
            val style = BaseStyle()
            lineApi.initLine(style)
            lineApi.printText(text + "\n", null)
            lineApi.printText(text + "\n", null)
            // Some versions use autoOut(), others might have a print method.
            // Looking at documentation, autoOut() is common for internal printers.
            lineApi.autoOut()
        } catch (e: Exception) {
            Log.e("SunmiPrinterX", "Printing error", e)
        }
    }

    fun deinitPrinter(context: Context) {
        printer = null
        Log.d("SunmiPrinterX", "deinitPrinter called")
    }
}
