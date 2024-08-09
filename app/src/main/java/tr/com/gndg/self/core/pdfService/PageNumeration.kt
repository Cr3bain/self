package tr.com.gndg.self.core.pdfService


import com.itextpdf.text.Anchor
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import org.joda.time.DateTime

internal class PageNumeration : PdfPageEventHelper() {
    override fun onEndPage(writer: PdfWriter, document: Document) {
        try {
            var cell: PdfPCell
            val table = PdfPTable(2)
            table.widthPercentage = 100f
            table.setWidths(floatArrayOf(3f, 1f))

            //1st Column
            val anchor = Anchor(
                Phrase(DateTime.now().toString("dd.MM.yyyy"), FONT_FOOTER)
            )
            anchor.reference = " "
            cell = PdfPCell(anchor)
            cell.horizontalAlignment = Element.ALIGN_LEFT
            cell.border = 0
            cell.setPadding(2f)
            table.addCell(cell)
            table.totalWidth =
                document.pageSize.width - document.leftMargin() - document.rightMargin()
            table.writeSelectedRows(
                0,
                -1,
                document.leftMargin(),
                document.bottomMargin() - 5,
                writer.directContent
            )

            //2nd Column
            cell = PdfPCell(Phrase("Page - " + writer.pageNumber.toString(), FONT_FOOTER))
            cell.horizontalAlignment = Element.ALIGN_RIGHT
            cell.border = 0
            cell.setPadding(2f)
            table.addCell(cell)
            table.totalWidth =
                document.pageSize.width - document.leftMargin() - document.rightMargin()
            table.writeSelectedRows(
                0,
                -1,
                document.leftMargin(),
                document.bottomMargin() - 5,
                writer.directContent
            )
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        private val TAG = PageNumeration::class.java.simpleName
        private val FONT_FOOTER: Font =
            Font(Font.FontFamily.TIMES_ROMAN, 8f, Font.NORMAL, BaseColor.DARK_GRAY)
    }
}