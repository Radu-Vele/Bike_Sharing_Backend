package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.utils.exceptions.IllegalOperationException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {
    @Override
    public void generatePDF(String[] content) throws IOException, IllegalOperationException {
        if(content.length == 0) {
            throw new IllegalOperationException("Cannot generate PDF with empty content");
        }

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 700);

        for(String dataLine : content) {
            contentStream.showText(dataLine);
            contentStream.newLine();
        }

        contentStream.endText();
        contentStream.close();

        document.save("./receipts/ride_"+ content[0] +".pdf");
        document.close();
    }

    @Override
    public String[] formatRide(Ride ride) {
        String title = Integer.toString(ride.getEndTime().getNano());
        String header = "Your ride on " + ride.getEndTime().getDayOfMonth() + " " + ride.getEndTime().getMonth();
        String line1 = "Start Station: " + ride.getStartStationName();
        String line2 = "End Station: " + ride.getEndStationName();
        String line3 = "Bike Id : " + ride.getBikeExternalId();

        return new String[]{title, header, line1, line2, line3};
    }

    @Override
    public void generateRideReceipt(Ride ride) throws IOException, IllegalOperationException {
        generatePDF(formatRide(ride));
    }

}
