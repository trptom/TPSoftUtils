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
    public static class DiffFile implements Comparable<DiffFile> {
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
        
        @Override
        public int compareTo(DiffFile o) {
            return getName().compareTo(o.getName());
        }
    }
    
    
    private String folder;

    public Subversion(String folder) {
        if (!folder.endsWith("\\")) {
            folder += "\\";
        }
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
    
    public int getCurrentRevision() throws IOException {
        return getCurrentRevision(null);
    }
    
    public int getCurrentRevision(String path) throws IOException {
        if (path.endsWith("\\")) {
            path = path.substring(0, path.length()-1);
        }
        
        String cmd = "cmd.exe /c d: & cd \"" + folder + "\" & svn info" + (path != null ? (" \"" + path + "\"") : "");
        
        System.out.println("processing: " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        
        try {
            int i = p.waitFor();
            System.out.println("process state: " + i);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return -1;
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = reader.readLine();
        String rev = null;
        
        while (line != null) {
            System.out.println(">> " + line);
            if (line.startsWith("Revision: ")) {
                rev = line.split("Revision: ")[1];
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
        if (path.endsWith("\\")) {
            path = path.substring(0, path.length()-1);
        }
        
        LinkedList<DiffFile> ret = new LinkedList<>();
        
        String cmd = "cmd.exe /c d: & cd \"" + folder + "\" & svn diff --summarize -r " + r1 + ":" + r2 + (path != null ? (" " + path) : "");
        
        System.out.println("processing: " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        
        try {
            int i = p.waitFor();
            System.out.println("process state: " + i);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return null;
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = reader.readLine();
        
        while (line != null) {
            System.out.println(">> " + line);
            
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
        
        System.out.println("processing finished");
        
        return ret;
    }
    
    public void update(String path) throws IOException {
        if (path.endsWith("\\")) {
            path = path.substring(0, path.length()-1);
        }
        
        String cmd = "cmd.exe /c d: & cd \"" + folder + "\" & svn update" + (path != null ? (" \"" + path + "\"") : "");
        
        System.out.println("processing: " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        
        try {
            int i = p.waitFor();
            System.out.println("process state: " + i);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return;
        }
    }
    
    public void addAll(String pathOrFile) throws IOException {
        String cmd = "cmd.exe /c d: & cd \"" + folder + "\" & svn add " + pathOrFile;
        
        System.out.println("processing: " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        
        try {
            int i = p.waitFor();
            System.out.println("process state: " + i);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return;
        }
    }
    
    public void commit(String path, String message) throws IOException {
        String cmd = "cmd.exe /c d: & cd \"" + folder + "\" & svn commit -m \"" + message + "\"" + (path != null ? (" " + path) : "");
        
        System.out.println("processing: " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        
        try {
            int i = p.waitFor();
            System.out.println("process state: " + i);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return;
        }
    }
}
