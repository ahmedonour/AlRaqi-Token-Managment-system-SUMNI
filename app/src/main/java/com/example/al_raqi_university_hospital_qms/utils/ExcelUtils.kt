package com.example.al_raqi_university_hospital_qms.utils

import android.content.Context
import android.net.Uri
import com.example.al_raqi_university_hospital_qms.data.HistoryItem
import com.example.al_raqi_university_hospital_qms.data.Section
import com.example.al_raqi_university_hospital_qms.data.Token
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import java.io.OutputStream

object ExcelUtils {

    fun generateTemplate(outputStream: OutputStream) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Sections Template")
        
        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Name")
        header.createCell(1).setCellValue("Type (Clinic/Laboratory)")
        header.createCell(2).setCellValue("Price")
        header.createCell(3).setCellValue("Doctors (Comma separated)")

        // Add an example row
        val example = sheet.createRow(1)
        example.createCell(0).setCellValue("General Medicine")
        example.createCell(1).setCellValue("Clinic")
        example.createCell(2).setCellValue("100")
        example.createCell(3).setCellValue("Dr. Smith, Dr. Doe")

        workbook.write(outputStream)
        workbook.close()
    }

    fun importSections(inputStream: InputStream): List<Section> {
        val sections = mutableListOf<Section>()
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)

        for (i in 1..sheet.lastRowNum) {
            val row = sheet.getRow(i) ?: continue
            val name = row.getCell(0)?.stringCellValue ?: ""
            val type = row.getCell(1)?.stringCellValue ?: "Clinic"
            val price = row.getCell(2)?.numericCellValue?.toInt() ?: 0
            val doctorsStr = row.getCell(3)?.stringCellValue ?: ""
            val doctors = if (doctorsStr.isNotEmpty()) doctorsStr.split(",").map { it.trim() } else emptyList()

            if (name.isNotEmpty()) {
                sections.add(
                    Section(
                        id = i, // Temporary ID, repository will assign new ones if needed
                        name = name,
                        type = type,
                        price = price,
                        queue = emptyList(),
                        doctors = doctors
                    )
                )
            }
        }
        workbook.close()
        return sections
    }

    fun exportHistory(history: List<HistoryItem>, outputStream: OutputStream, timeframe: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Report $timeframe")

        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Section Name")
        header.createCell(1).setCellValue("Token Number")
        header.createCell(2).setCellValue("Price")
        header.createCell(3).setCellValue("Timestamp")

        history.forEachIndexed { index, item ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(item.sectionName)
            row.createCell(1).setCellValue(item.tokenNumber.toDouble())
            row.createCell(2).setCellValue(item.price.toDouble())
            row.createCell(3).setCellValue(java.util.Date(item.timestamp).toString())
        }

        workbook.write(outputStream)
        workbook.close()
    }
}
