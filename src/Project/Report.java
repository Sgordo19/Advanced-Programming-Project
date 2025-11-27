package Project;

import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    //System.out.println("Entry added to Report #" + report_id);
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
public void exportToPDF(String filePath) throws Exception {
    PDDocument document = new PDDocument();
    PDPage page = new PDPage(PDRectangle.A4);
    document.addPage(page);

    PDPageContentStream content = new PDPageContentStream(document, page);
    
    PDFont boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    PDFont regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    
    // Title
    content.setFont(boldFont, 18);
    content.beginText();
    content.newLineAtOffset(50, 750);
    content.showText(type + " Report");
    content.endText();

    // Date range
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    content.setFont(regularFont, 12);
    content.beginText();
    content.newLineAtOffset(50, 730);
    content.showText("Period: " + sdf.format(date_from) + " to " + sdf.format(date_to));
    content.endText();

    // Generated timestamp
    content.beginText();
    content.newLineAtOffset(50, 715);
    content.showText("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(generated_at));
    content.endText();

    // Separator line
    content.moveTo(50, 705);
    content.lineTo(545, 705);
    content.stroke();

    float yPosition = 685;
    int entryCount = 1;

    // Handle different report types
    for (T entry : entries) {
        if (entry instanceof Shipment) {
            // ========== SHIPMENT REPORT ==========
            yPosition = renderShipmentEntry(content, document, (Shipment) entry, 
                                           yPosition, entryCount, boldFont, regularFont);
            entryCount++;
            
        } else if (entry instanceof String) {
            // ========== STRING-BASED REPORTS ==========
            // (Delivery Performance, Revenue, Vehicle Utilization)
            yPosition = renderStringEntry(content, document, (String) entry, 
                                         yPosition, boldFont, regularFont);
        }
    }

    // Summary footer
    if (yPosition < 60) {
        content.close();
        page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        content = new PDPageContentStream(document, page);
        yPosition = 750;
    }

    content.setFont(boldFont, 11);
    content.beginText();
    content.newLineAtOffset(50, yPosition - 10);
    content.showText("Total Entries: " + entries.size());
    content.endText();

    content.close();
    
    // Ensure parent directory exists
    File outputFile = new File(filePath);
    File parentDir = outputFile.getParentFile();
    if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs();
    }
    
    document.save(outputFile);
    document.close();
}

// Helper method to render Shipment entries
private float renderShipmentEntry(PDPageContentStream content, PDDocument document, 
                                  Shipment shipment, float yPosition, int entryCount,
                                  PDFont boldFont, PDFont regularFont) throws Exception {
    // Check if we need a new page (reserve 130 units for each shipment entry)
    if (yPosition < 130) {
        content.close();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        content = new PDPageContentStream(document, page);
        yPosition = 750;
    }

    // Entry number/header
    content.setFont(boldFont, 11);
    content.beginText();
    content.newLineAtOffset(50, yPosition);
    content.showText("Shipment #" + entryCount + " - " + shipment.getTrackingNumber());
    content.endText();
    yPosition -= 18;

    // Status and Type
    content.setFont(regularFont, 10);
    content.beginText();
    content.newLineAtOffset(50, yPosition);
    content.showText("Status: " + shipment.getStatus() + 
                   "  |  Type: " + shipment.getPackageType());
    content.endText();
    yPosition -= 15;

    // Sender info
    content.beginText();
    content.newLineAtOffset(50, yPosition);
    content.showText("Sender: " + (shipment.getSender() != null ? 
                   shipment.getSender().getUserID() : "N/A"));
    content.endText();
    yPosition -= 15;

    // Recipient info
    String recipientInfo = "Recipient: ";
    if (shipment.getRecipient() != null) {
        recipientInfo += shipment.getRecipient().getName();
        if (shipment.getRecipient().getPhoneNumber() != null) {
            recipientInfo += " - " + shipment.getRecipient().getPhoneNumber();
        }
    } else {
        recipientInfo += "N/A";
    }
    content.beginText();
    content.newLineAtOffset(50, yPosition);
    content.showText(recipientInfo);
    content.endText();
    yPosition -= 15;

    // Package details
    String packageInfo = "Package: ";
    if (shipment.getPkg() != null) {
        packageInfo += String.format("%.2f kg, %.1fx%.1fx%.1f cm", 
            shipment.getPkg().getWeight(),
            shipment.getPkg().getLength(),
            shipment.getPkg().getWidth(),
            shipment.getPkg().getHeight());
    } else {
        packageInfo += "N/A";
    }
    content.beginText();
    content.newLineAtOffset(50, yPosition);
    content.showText(packageInfo);
    content.endText();
    yPosition -= 15;

    // Cost and distance
    content.beginText();
    content.newLineAtOffset(50, yPosition);
    content.showText(String.format("Cost: $%.2f  |  Distance: %.2f km", 
                   shipment.getCost(), shipment.getDistance()));
    content.endText();
    yPosition -= 15;

    // Dates
    content.beginText();
    content.newLineAtOffset(50, yPosition);
    content.showText("Created: " + shipment.getCreationDate() + 
                   "  |  Delivered: " + 
                   (shipment.getDeliveryDate() != null ? 
                    shipment.getDeliveryDate() : "Pending"));
    content.endText();
    yPosition -= 15;

    // Vehicle assignment
    content.beginText();
    content.newLineAtOffset(50, yPosition);
    content.showText("Assigned Vehicle: " + 
                   (shipment.getAssignedVehicleId() > 0 ? 
                    "#" + shipment.getAssignedVehicleId() : "Not Assigned"));
    content.endText();
    yPosition -= 20;

    // Separator line between entries
    content.setLineWidth(0.5f);
    content.moveTo(50, yPosition);
    content.lineTo(545, yPosition);
    content.stroke();
    yPosition -= 15;

    return yPosition;
}

// Helper method to render String entries (for other report types)
private float renderStringEntry(PDPageContentStream content, PDDocument document,
                                String text, float yPosition, 
                                PDFont boldFont, PDFont regularFont) throws Exception {
    // Check if we need a new page
    if (yPosition < 50) {
        content.close();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        content = new PDPageContentStream(document, page);
        yPosition = 750;
    }

    // Determine if this is a header line (Vehicle #X) or a data line
    boolean isHeader = text.startsWith("Vehicle #") || 
                      text.startsWith("Total Shipments:") ||
                      text.startsWith("Total Revenue:");
    
    // Use bold for headers, regular for data
    content.setFont(isHeader ? boldFont : regularFont, 
                   isHeader ? 11 : 10);
    
    // Indent sub-items (lines starting with spaces)
    float xOffset = text.startsWith("  ") ? 70 : 50;
    
    content.beginText();
    content.newLineAtOffset(xOffset, yPosition);
    content.showText(text.trim());
    content.endText();
    
    // Add extra spacing after headers
    yPosition -= isHeader ? 18 : 15;
    
    return yPosition;
}

}
