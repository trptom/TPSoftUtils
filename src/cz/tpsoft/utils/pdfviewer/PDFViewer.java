package cz.tpsoft.utils.pdfviewer;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import cz.tpsoft.utils.pdfviewer.exception.AWTDesktopNotSupportedException;
import cz.tpsoft.utils.pdfviewer.exception.NotPDFFileException;

/**
 * Trida, slouzici k otvirani PDF souboru.
 * @author Tomas Praslicak
 */
public class PDFViewer {
    public static final String FILE_NAME = "webconf.pdf";

    /**
     * Zjisti, zda nazev souboru konci priponou "pdf". Neni case-sensitive.
     * @param fileName adresa souboru.
     * @return true, pokud soubor konci priponou "pdf", jinak false.
     */
    public static boolean isPDF(String fileName) {
        return isPDF(new File(fileName));
    }
    
    /**
     * Zjisti, zda nazev souboru konci priponou "pdf". Neni case-sensitive.
     * @param file zkoumany soubor.
     * @return true, pokud nazev souboru konci priponou "pdf", jinak false.
     */
    public static boolean isPDF(File file) {
        return file.getAbsolutePath().toLowerCase().endsWith(".pdf");
    }
    
    
    
    public synchronized static void openPDF(File file)
            throws IOException, InterruptedException,
            AWTDesktopNotSupportedException {
        if (file.exists()) {
            if (isPDF(file)) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    throw new AWTDesktopNotSupportedException();
                }
            } else {
                throw new NotPDFFileException(file);
            }
        } else {
            throw new FileNotFoundException();
        }
    }
    
    public static void downloadRemotePDF(String pdfUrl) {
    }
}
