package tr.com.gndg.self.core.pdfService

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.joda.time.DateTime
import tr.com.gndg.self.core.fileProvider.getPDFDirectory
import tr.com.gndg.self.core.pdfService.models.ArrivalPdfModel
import tr.com.gndg.self.core.pdfService.models.OutgoingPdfModel
import tr.com.gndg.self.core.util.bigDecimalToString
import tr.com.gndg.self.core.util.floatToString
import tr.com.gndg.self.domain.join.ProductsJoin
import tr.com.gndg.self.domain.model.Stock
import java.io.File
import java.io.FileOutputStream


class PdfService {

    private val fontFactory: Font = FontFactory
        .getFont("assets/fonts/roboto_regular.ttf",
            "windows-1254",
            BaseFont.EMBEDDED)

    private val titleFont = Font(fontFactory.baseFont, 16f, Font.BOLD)
    private val bodyFont = Font(fontFactory.baseFont, 12f, Font.NORMAL)
    private val cellFont = Font(fontFactory.baseFont, 10f, Font.NORMAL)
    private lateinit var pdf: PdfWriter

    private fun createFile(context: Context, fileName: String): File {
        //Prepare file
        val path = getPDFDirectory(context)//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, fileName)
        if (!file.exists()) file.createNewFile()
        return file
    }

    private fun createDocument(): Document {
        //Create Document
        val document = Document()
        document.setMargins(24f, 24f, 32f, 32f)
        document.pageSize = PageSize.A4
        return document
    }

    private fun setupPdfWriter(document: Document, file: File) {
        pdf = PdfWriter.getInstance(document, FileOutputStream(file))
        //pdf.setPdfVersion(PdfWriter.PDF_VERSION_1_7)
        //pdf.setFullCompression()
        pdf.pageEvent =  PageNumeration()
        //Open the document
        document.open()

        pdf.directContent.setFontAndSize(fontFactory.baseFont, 12f)
    }

    private fun createTable(column: Int, columnWidth: FloatArray): PdfPTable {
        val table = PdfPTable(column)
        table.widthPercentage = 100f
        table.setWidths(columnWidth)
        table.headerRows = 1
        table.defaultCell.verticalAlignment = Element.ALIGN_CENTER
        table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        return table
    }

    private fun createCell(content: String): PdfPCell {
        val cell = PdfPCell(Phrase(content, cellFont))
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        //setup padding
        cell.setPadding(8f)
        cell.isUseAscender = true
        cell.paddingLeft = 4f
        cell.paddingRight = 4f
        cell.paddingTop = 8f
        cell.paddingBottom = 8f

        return cell
    }

    private fun createImageCell(fileUri: String): PdfPCell? {
        return try {
            val image: Image =
                Image.getInstance(fileUri)
            image.alignment = Element.ALIGN_CENTER
            image.setRotationDegrees(-90f)
            val preferredImageHeight = 54f
            val widthScale: Float = image.scaledHeight / preferredImageHeight
            image.scaleAbsolute(image.scaledWidth / widthScale, preferredImageHeight)
            PdfPCell(image)
        } catch (e: Exception) {
            null
        }

    }

    private fun addLineSpace(document: Document, number: Int) {
        for (i in 0 until number) {
            document.add(Paragraph(" "))
        }
    }

    private fun createParagraph(content: String): Paragraph{
        val paragraph = Paragraph(content, bodyFont)
        paragraph.firstLineIndent = 25f
        paragraph.alignment = Element.ALIGN_JUSTIFIED
        return paragraph
    }

    fun createArrivalListTable(
        tableHeaderContent : List<String>,
        data: List<ArrivalPdfModel>,
    ) : PdfPTable {
        //Define Table
        val documentCellW = 0.4f
        val dataWidth = 0.4f
        val skuWidth = 0.3f
        val productNameWidth = 1f
        val supplierNameWidth = 0.5f
        val warehouseNameW = 0.5f
        val pieceW = 0.5f
        val unitPrice = 0.5f
        val totalPrice = 0.5f

        val columnWidth = floatArrayOf(documentCellW, dataWidth, skuWidth, productNameWidth, supplierNameWidth, warehouseNameW, pieceW, unitPrice, totalPrice)
        val table = createTable(tableHeaderContent.size, columnWidth)

        //write table header into table
        tableHeaderContent.forEach {
            //define a cell
            val cell = createCell(it)
            //add our cell into our table
            table.addCell(cell)
        }
        //write user data into table
        data.forEach {
            //Write Each User Id

            val documentCell = createCell(it.documentNumber?:"")
            table.addCell(documentCell)

            val dateCell = createCell(it.date)
            table.addCell(dateCell)

            val skuCell = createCell(it.sku?:"")
            table.addCell(skuCell)

            val productNameCell = createCell(it.productName)
            table.addCell(productNameCell)

            val supplierNameCell = createCell(it.supplierName?:"")
            table.addCell(supplierNameCell)

            val warehouseNameCell = createCell(it.warehouseName)
            table.addCell(warehouseNameCell)

            val pieceCell = createCell(floatToString(it.piece))
            table.addCell(pieceCell)

            val unitPriceCell = createCell(bigDecimalToString(it.unitPrice))
            table.addCell(unitPriceCell)

            val totalPriceCell = createCell(bigDecimalToString(it.totalPrice))
            table.addCell(totalPriceCell)
        }

        return table
    }

    fun createOutgoingListTable(
        tableHeaderContent : List<String>,
        data: List<OutgoingPdfModel>,
    ) : PdfPTable {
        //Define Table
        val documentCellW = 0.4f
        val dataWidth = 0.4f
        val skuWidth = 0.3f
        val productNameWidth = 1f
        val supplierNameWidth = 0.5f
        val warehouseNameW = 0.5f
        val pieceW = 0.5f
        val unitPrice = 0.5f
        val totalPrice = 0.5f

        val columnWidth = floatArrayOf(documentCellW, dataWidth, skuWidth, productNameWidth, supplierNameWidth, warehouseNameW, pieceW, unitPrice, totalPrice)
        val table = createTable(tableHeaderContent.size, columnWidth)

        //write table header into table
        tableHeaderContent.forEach {
            //define a cell
            val cell = createCell(it)
            //add our cell into our table
            table.addCell(cell)
        }
        //write user data into table
        data.forEach {
            //Write Each User Id

            val documentCell = createCell(it.documentNumber?:"")
            table.addCell(documentCell)

            val dateCell = createCell(it.date)
            table.addCell(dateCell)

            val skuCell = createCell(it.sku?:"")
            table.addCell(skuCell)

            val productNameCell = createCell(it.productName)
            table.addCell(productNameCell)

            val customerNameCell = createCell(it.customerName?:"")
            table.addCell(customerNameCell)

            val warehouseNameCell = createCell(it.warehouseName)
            table.addCell(warehouseNameCell)

            val pieceCell = createCell(floatToString(it.piece))
            table.addCell(pieceCell)

            val unitPriceCell = createCell(bigDecimalToString(it.unitPrice))
            table.addCell(unitPriceCell)

            val totalPriceCell = createCell(bigDecimalToString(it.totalPrice))
            table.addCell(totalPriceCell)
        }

        return table
    }

    fun createStockListTable(
        tableHeaderContent : List<String>,
        data: List<ProductsJoin>,
        warehouseID: Long?,
    ) : PdfPTable {
        //Define Table
        //val imageWidth = 0.4f
        val skuWidth = 0.5f
        val productNameWidth = 1f
        val stockWidth = 0.5f
        val columnWidth = floatArrayOf(skuWidth, productNameWidth, stockWidth)
        val table = createTable(tableHeaderContent.size, columnWidth)

        //write table header into table
        tableHeaderContent.forEach {
            //define a cell
            val cell = createCell(it)
            //add our cell into our table
            table.addCell(cell)
        }
        //write user data into table
        data.forEach {
            //Write Each User Id

/*            if (it.imageUri != null) {
                val imageCell = createImageCell(it.imageUri)
                table.addCell(imageCell)
            } else {
                val idCell = createCell("")
                table.addCell(idCell)
            }*/
            val skuCell = createCell(it.product.stockCode?:"")
            table.addCell(skuCell)
            val productNameCell = createCell(it.product.name)
            table.addCell(productNameCell)

            val stockList = it.stock

            val stock = productPiece(warehouseID = warehouseID, stockList = stockList)
            val stockCell = createCell(stock?.toString()?:"0.0")
            table.addCell(stockCell)

        }

        return table
    }

    private fun productPiece(
        warehouseID : Long?,
        stockList : List<Stock>
    ) : Float? {
        return if (warehouseID == null || warehouseID == 0L) {
            var piece = 0F
            stockList.forEach {
                piece += it.piece?:0F
            }
            piece
        } else {
            //WE HAVE WAREHOUSE UUID. Probably from StockStateScreen
            stockList.find { i-> i.warehouseID == warehouseID }?.piece
        }
    }

    fun createProductListTable(
        tableHeaderContent : List<String>,
        data: List<ProductsJoin>,
    ) : PdfPTable {
        //Define Table
        //val imageWidth = 0.4f
        val skuWidth = 0.3f
        val barcodeWidth = 0.5f
        val productNameWidth = 1f
        val desc = 0.6f
        val columnWidth = floatArrayOf(skuWidth, barcodeWidth, productNameWidth, desc)
        val table = createTable(tableHeaderContent.size, columnWidth)

        //write table header into table
        tableHeaderContent.forEach {
            //define a cell
            val cell = createCell(it)
            //add our cell into our table
            table.addCell(cell)
        }
        //write user data into table
        data.forEach {
            //Write Each User Id

/*            if (it.imageUri != null) {
                val imageCell = createImageCell(it.imageUri)
                table.addCell(imageCell)
            } else {
                val idCell = createCell("")
                table.addCell(idCell)
            }*/
            val skuCell = createCell(it.product.stockCode?:"")
            table.addCell(skuCell)
            val barcodeCell = createCell(it.product.barcode?:"")
            table.addCell(barcodeCell)
            val productNameCell = createCell(it.product.name)
            table.addCell(productNameCell)
            val descriptionCell = createCell(it.product.description?:"")
            table.addCell(descriptionCell)
        }

        return table

    }


    fun createPDF(
        context: Context,
        fileName: String = DateTime.now().millis.toString(),
        table : PdfPTable,
        pageTitle: String? = null,
        tableTitle: String? = null,
        paragraphLines: List<String>? = null,
        onFinish: (file: File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        //Define the document
        val file = createFile(context, fileName)
        val document = createDocument()

        //Setup PDF Writer
        setupPdfWriter(document, file)

        //Add Title
        if (pageTitle != null) {
            document.add(Paragraph(pageTitle, titleFont))
            //Add Empty Line as necessary
            addLineSpace(document, 1)
        }

        //Add paragraph
        paragraphLines?.forEach {content->
            val paragraph = createParagraph(content)
            document.add(paragraph)
        }
        addLineSpace(document, 1)
        //Add Empty Line as necessary

        //Add table title
        if (tableTitle != null) {
            document.add(Paragraph(tableTitle, titleFont))
            //Add Empty Line as necessary
            addLineSpace(document, 1)
        }
        document.add(table)

        pdf.pageEvent.onEndPage(
            pdf, document
        )

        document.close()

        try {
            pdf.close()
        } catch (ex: Exception) {
            onError(ex)
        } finally {
            onFinish(file)
        }
    }
}