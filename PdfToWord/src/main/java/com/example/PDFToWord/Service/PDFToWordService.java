package com.example.PDFToWord.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PDFToWordService {

    public String convertPdfToWord(String pdfPath, String outputPath) throws IOException {
        // Load PDF and extract text
        PDDocument document = PDDocument.load(new File(pdfPath));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();

        // Create Word document
        try (XWPFDocument wordDocument = new XWPFDocument()) {
            XWPFParagraph paragraph = wordDocument.createParagraph();
            paragraph.createRun().setText(text);

            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                wordDocument.write(out);
            }
        }

        return "Conversion successful. Word document saved at: " + outputPath;
    }
}
