/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.svn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 *
 * @author tomas.praslicak
 */
public class Subversion {
    public static class DiffFile {
        public static enum Status {
            MODIFIED,
            ADDED,
            DELETED
        }
        
        private String name;
        private Status status;

        public DiffFile(String name, Status status) {
            this.name = name;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public Status getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return getName() + ": " + getStatus().toString();
        }
    }
    
    
    private String folder;

    public Subversion(String folder) {
        if (!folder.endsWith("\\")) {
            folder += "\\";
        }
        this.folder = folder;
    }
    
    public int getCurrentRevision() throws IOException {
        return getCurrentRevision(null);
    }
    
    public int getCurrentRevision(String path) throws IOException {
        Process p = Runtime.getRuntime().exec(
            "cmd.exe /c d: & cd \"" + folder + "\" & svn info" + (path != null ? (" \"" + path + "\"") : "")
        );
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = reader.readLine();
        String rev = null;
        
        while (line != null) {
            System.out.println(line);
            if (line.startsWith("Revision: ")) {
                rev = line.split("Revision: ")[1];
                System.out.println(line);
                line = null;
            } else {
                line = reader.readLine();
            }
        }
        
        if (rev != null) {
            return Integer.parseInt(rev, 10);
        } else {
            return -1;
        }
    }
    
    public LinkedList<DiffFile> diff(int r1, int r2, String path) throws IOException {
        LinkedList<DiffFile> ret = new LinkedList<>();
        
        Process p = Runtime.getRuntime().exec(
            "cmd.exe /c d: & cd \"" + folder + "\" & svn diff --summarize -r " + r1 + ":" + r2 + (path != null ? (" \"" + path + "\"") : "")
        );
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = reader.readLine();
        
        while (line != null) {
            String fileName = line.substring(8);
            String atts = line.substring(0, 8);
            DiffFile.Status status = null;
            
            if (atts.contains("A")) {
                status = DiffFile.Status.ADDED;
            } else if (atts.contains("D")) {
                status = DiffFile.Status.DELETED;
            } else if (atts.contains("M")) {
                status = DiffFile.Status.MODIFIED;
            }
            
            if (status != null) {
                ret.addLast(new DiffFile(fileName, status));
                System.out.println(ret.getLast().toString());
            } else {
                System.err.println("unknown status for file \"" + fileName + "\"");
            }
            
            line = reader.readLine();
        }
        
        return ret;
    }
    
    public static void main(String[] args) {
        Subversion svn = new Subversion("d:\\Dokumenty\\NetBeansProjects\\MSDS Profi Manager\\");
        try {
            System.out.println(svn.getCurrentRevision());
            
            svn.diff(100, 99, null);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
