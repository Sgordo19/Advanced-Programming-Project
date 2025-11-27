package Project;

import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class Report<T> {

private int report_id;
private Date date_from;
private Date date_to;
private Date generated_at;
private String type;
private ArrayList<T> entries;

// Default constructor
public Report() {
    this.report_id = 0;
    this.date_from = new Date();
    this.date_to = new Date();
    this.generated_at = new Date();
    this.type = "";
    this.entries = new ArrayList<>();
}

// Primary constructor
public Report(int report_id, Date date_from, Date date_to, String type) {
    this.report_id = report_id;
    this.date_from = date_from;
    this.date_to = date_to;
    this.generated_at = new Date();
    this.type = type;
    this.entries = new ArrayList<>();
}

// Getters and setters
public int getReport_id() {
    return report_id;
}

public void setReport_id(int report_id) {
    this.report_id = report_id;
}

public Date getDate_from() {
    return date_from;
}

public void setDate_from(Date date_from) {
    this.date_from = date_from;
}

public Date getDate_to() {
    return date_to;
}

public void setDate_to(Date date_to) {
    this.date_to = date_to;
}

public Date getGenerated_at() {
    return generated_at;
}

public void setGenerated_at(Date generated_at) {
    this.generated_at = generated_at;
}

public String getType() {
    return type;
}

public void setType(String type) {
    this.type = type;
}

public ArrayList<T> getEntries() {
    return entries;
}

public void addEntry(T entry) {
    entries.add(entry);
    System.out.println("Entry added to Report #" + report_id);
}

// Print report to console
public void printReport() {
    System.out.println("\n==== REPORT #" + report_id + " ====");
    System.out.println("Type: " + type);
    System.out.println("Generated At: " + generated_at);
    System.out.println("Date Range: " + date_from + " to " + date_to);
    System.out.println("Entries:");
    for (T entry : entries) {
        System.out.println(entry.toString());
    }
}

// Export report to PDF
public void exportToPDF(String filePath) {
    PDDocument document = new PDDocument();
    PDPage page = new PDPage(PDRectangle.A4);
    document.addPage(page);
    try {
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        int y = 770;

        // Report header
        contentStream.setFont(font, 18);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText("REPORT #" + report_id);
        contentStream.endText();

        y -= 30;
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText("Type: " + type);
        contentStream.endText();

        y -= 20;
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText("Generated At: " + generated_at);
        contentStream.endText();

        y -= 20;
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText("Date Range: " + date_from + " to " + date_to);
        contentStream.endText();

        // Entries
        y -= 30;
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText("Entries:");
        contentStream.endText();

        for (T entry : entries) {
            y -= 20;
            if (y < 50) {
                break; // prevent overflow
            }
            contentStream.beginText();
            contentStream.newLineAtOffset(70, y);
            contentStream.showText(entry.toString());
            contentStream.endText();
        }

        contentStream.close();
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // create folders if needed
        document.save(file);
       // document.save(new File(filePath));
        System.out.println("Report exported to PDF: " + filePath);

    } catch (IOException e) {
        System.err.println("Failed to export report to PDF: " + e.getMessage());
    } finally {
        try {
            document.close();
        } catch (IOException e) {}
    }
}

}
