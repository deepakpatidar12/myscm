package com.myScm.scm.serviceImpl;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.myScm.scm.Dto.ContactForm;
import com.myScm.scm.services.ExportService;
import org.springframework.stereotype.Service;

@Service
public class ExportServiceImpl implements ExportService {

        @Override
        public ByteArrayInputStream generatePdf(List<ContactForm> contacts) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(byteArrayOutputStream);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf, PageSize.A4);

                document.setMargins(30, 40, 50, 40);

                // Add headers and footers with borders
                pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageEventHandler());

                // Add content to the PDF
                for (ContactForm contact : contacts) {
                        Style titleStyle = new Style().setFontSize(18).setBold();
                        // Style subtitleStyle = new Style().setFontSize(14).setItalic();
                        Style normalStyle = new Style().setFontSize(14);
                        // Style boldStyle = new Style().setFontSize(12).setBold();
                        Style italicStyle = new Style().setFontSize(12).setItalic();

                        // Add name and nickname
                        String name = contact.getName();
                        String nickname = contact.getNickName();
                        document.add(new Paragraph(name).addStyle(titleStyle).add(" (").add(nickname).add(")")
                                        .addStyle(italicStyle));

                        try {
                                ImageData imageData = ImageDataFactory.create(contact.getContactPic());
                                Image image = new Image(imageData).scaleToFit(100, 100);
                                document.add(image);
                        } catch (Exception e) {
                                document.add(new Paragraph("Image not available"));
                        }

                        // Add email, phone, address, about
                        document.add(new Paragraph("Email: ").add(contact.getContactEmail()).addStyle(normalStyle));
                        document.add(new Paragraph("Phone: ").add(contact.getContactNumber())
                                        .addStyle(normalStyle));
                        document.add(new Paragraph("Address: ").add(contact.getAddress()).addStyle(normalStyle));

                        // Add a page break if necessary (for multiple contacts)
                        document.add(new Paragraph("\n\n\n"));

                }
                document.close();

                // Prepare response
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        }

        private static class PageEventHandler implements IEventHandler {

                @Override
                public void handleEvent(Event event) {
                        PdfDocumentEvent pdfEvent = (PdfDocumentEvent) event;
                        PdfDocument pdf = pdfEvent.getDocument();
                        PdfPage page = pdfEvent.getPage();

                        float margin = 40;

                        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
                        pdfCanvas.saveState();
                        pdfCanvas.setLineWidth(1f);

                        pdfCanvas.moveTo(margin, margin).lineTo(page.getPageSize().getWidth() - margin, margin)
                                        .stroke();
                        pdfCanvas.restoreState();

                        // Add page number at the bottom
                        int pageNumber = pdf.getPageNumber(page);
                        Rectangle pageSize = page.getPageSize();
                        float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
                        float y = pageSize.getBottom() + 30; // Adjust this value as needed

                        try {
                                pdfCanvas.beginText().setFontAndSize(PdfFontFactory.createFont(), 10).moveText(x, y)
                                                .showText(String.valueOf(pageNumber))
                                                .endText();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }

                        if (pdfEvent.getType() == PdfDocumentEvent.START_PAGE) {
                                // Add top center text only on the first page
                                if (pdf.getPageNumber(page) == 1) {
                                        addTopCenterText(page, pdf);
                                }

                        }

                }

                private void addTopCenterText(PdfPage page, PdfDocument pdf) {
                        Rectangle pageSize = page.getPageSize();
                        float x = ((pageSize.getLeft()) + pageSize.getRight() - 90) / 2;
                        float y = pageSize.getTop() - 30;

                        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
                        try {
                                pdfCanvas.beginText().setFontAndSize(PdfFontFactory.createFont(), 20)
                                                .moveText(x, y)
                                                .showText("Contact List")
                                                .endText();
                        } catch (IOException e) {
                                
                        }
                }

        }

        @Override
        public ByteArrayInputStream generateExcel(List<ContactForm> contacts) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Contacts");

                // Create a header row
                Row headerRow = sheet.createRow(0);
                String[] headers = { "Name", "Nickname", "Email", "Phone", "Address" };
                for (int i = 0; i < headers.length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(headers[i]);
                        cell.setCellStyle(createHeaderCellStyle(workbook));
                }

                // Create data rows
                int rowNum = 1;
                for (ContactForm contact : contacts) {
                        Row row = sheet.createRow(rowNum++);

                        row.createCell(0).setCellValue(contact.getName());
                        row.createCell(1).setCellValue(contact.getNickName());
                        row.createCell(2).setCellValue(contact.getContactEmail());
                        row.createCell(3).setCellValue(contact.getContactNumber());
                        row.createCell(4).setCellValue(contact.getAddress());
                }

                // Resize all columns to fit the content size
                for (int i = 0; i < headers.length; i++) {
                        sheet.autoSizeColumn(i);
                }

                // Write the output to a byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                        workbook.write(baos);
                        workbook.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }

                // Prepare response
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

                return bais;
        }

        private CellStyle createHeaderCellStyle(Workbook workbook) {
                CellStyle cellStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                cellStyle.setFont(font);
                return cellStyle;
        }
}
