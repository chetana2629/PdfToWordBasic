package com.example.PDFToWord.controller;

import com.example.PDFToWord.Service.PDFToWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/convert")
public class PDFToWordController {

    @Autowired
    private PDFToWordService pdfToWordService;

    @PostMapping("/pdf-to-word")
    public ResponseEntity<Map<String, String>> convertPdfToWord(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "File not provided or invalid format.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String tempPdfPath = System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename();
        File tempFile = new File(tempPdfPath);
        file.transferTo(tempFile);

        String outputPath = tempPdfPath.replace(".pdf", ".docx");
        String message = pdfToWordService.convertPdfToWord(tempPdfPath, outputPath);

        Map<String, String> response = new HashMap<>();
        response.put("message", "File converted successfully.");
        response.put("filePath", outputPath);  
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> downloadFile(@RequestParam("filePath") String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
    }
}
