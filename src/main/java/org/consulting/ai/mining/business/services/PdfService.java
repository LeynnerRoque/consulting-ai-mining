package org.consulting.ai.mining.business.services;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class PdfService {
    public String extractTextFromPdf(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("O arquivo de currículo não pode ser nulo.");
        }

        byte[] pdfBytes = inputStream.readAllBytes();

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
