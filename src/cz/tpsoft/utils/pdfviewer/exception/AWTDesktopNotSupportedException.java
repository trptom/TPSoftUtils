/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.pdfviewer.exception;

/**
 *
 * @author tomas.praslicak
 */
public class AWTDesktopNotSupportedException extends RuntimeException {

    public AWTDesktopNotSupportedException() {}

    public AWTDesktopNotSupportedException(String message) {
        super(message);
    }

    public AWTDesktopNotSupportedException(Throwable cause) {
        super(cause);
    }

    public AWTDesktopNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
