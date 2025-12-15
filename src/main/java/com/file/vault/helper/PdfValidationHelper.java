package com.file.vault.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfValidationHelper {

    private static final byte[] PDF_MAGIC = {
            0x25, 0x50, 0x44, 0x46, 0x2D
    };

    public boolean isPdf(MultipartFile file) {
        // 1. Try filename extension
        String filename = file.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
            return true;
        }

        // 2. Fallback to magic byte validation
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[PDF_MAGIC.length];
            int read = is.read(header);
            if (read < PDF_MAGIC.length) {
                return false;
            }
            for (int i = 0; i < PDF_MAGIC.length; i++) {
                if (header[i] != PDF_MAGIC[i]) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
