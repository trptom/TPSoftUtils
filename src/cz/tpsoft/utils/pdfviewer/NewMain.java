/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.pdfviewer;

import java.io.File;
import java.io.IOException;
import cz.tpsoft.utils.network.NetWork;

/**
 *
 * @author tomas.praslicak
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            NetWork.download("www.gracilis.cz", "gracilis", "rokservis09", "info.php", new File("pokus.php"));
        }catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
