/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.pdfviewer.exception;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author tomas.praslicak
 */
public class NotPDFFileException extends IOException {

    public NotPDFFileException(File file, String message) {
        super(file.getName() + " : " + message);
    }

    public NotPDFFileException(File file) {
        super(file.getName());
    }
}
