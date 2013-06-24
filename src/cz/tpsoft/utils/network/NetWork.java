/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.network;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author tomas.praslicak
 */
public class NetWork {

    /**
     * Upload a file to a FTP server. A FTP URL is generated with the following
     * syntax: ftp://user:password@host:port/filePath;type=i.
     *
     * @param ftpServer , FTP server address (optional port ':portNumber').
     * @param user , Optional user name to login.
     * @param password , Optional password for user.
     * @param fileName , Destination file name on FTP server (with optional
     * preceding relative path, e.g. "myDir/myFile.txt").
     * @param source , Source file to upload.
     * @throws MalformedURLException, IOException on error.
     */
    public static void upload(String ftpServer, String user, String password,
            String fileName, File source) throws MalformedURLException,
            IOException {
        if (ftpServer != null && fileName != null && source != null) {
            StringBuilder sb = new StringBuilder("ftp://");
            // check for authentication else assume its anonymous access.
            if (user != null && password != null) {
                sb.append(user);
                sb.append(':');
                sb.append(password);
                sb.append('@');
            }
            sb.append(ftpServer);
            sb.append('/');
            sb.append(fileName);
            /*
             * type ==&gt; a=ASCII mode, i=image (binary) mode, d= file
             * directory listing
             */
            sb.append(";type=i");

            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                URL url = new URL(sb.toString());
                URLConnection urlc = url.openConnection();

                bos = new BufferedOutputStream(urlc.getOutputStream());
                bis = new BufferedInputStream(new FileInputStream(source));

                int i;
                // read byte by byte until end of stream
                while ((i = bis.read()) != -1) {
                    bos.write(i);
                }
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Input not available.");
        }
    }
    /**
    * Download a file from a FTP server. A FTP URL is generated with the following
    * syntax: ftp://user:password@host:port/filePath;type=i.
    *
    * @param ftpServer , FTP server address (optional port ':portNumber').
    * @param user , Optional user name to login.
    * @param password , Optional password for user.
    * @param fileName , Name of file to download (with optional preceeding relative
    * path, e.g. one/two/three.txt).
    * @param destination , Destination file to save.
    * @throws MalformedURLException, IOException on error.
    */
    public static void download(String ftpServer, String user, String password,
            String fileName, File destination) throws MalformedURLException,
            IOException {
        if (ftpServer != null && fileName != null && destination != null) {
            StringBuilder sb = new StringBuilder("ftp://");
            // check for authentication else assume its anonymous access.
            if (user != null && password != null) {
                sb.append(user);
                sb.append(':');
                sb.append(password);
                sb.append('@');
            }
            sb.append(ftpServer);
            sb.append('/');
            sb.append(fileName);
            /*
             * type ==&gt; a=ASCII mode, i=image (binary) mode, d= file
             * directory listing
             */
            sb.append(";type=i");
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                if (destination.exists()) {
                    destination.delete();
                }
                destination.createNewFile();
                
                URL url = new URL(sb.toString());
                URLConnection urlc = url.openConnection();

                bis = new BufferedInputStream(urlc.getInputStream());
                bos = new BufferedOutputStream(new FileOutputStream(
                        destination));

                int i;

                while ((i = bis.read()) != -1) {
                    bos.write(i);
                }
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Input not available");
        }
    }
    
    public static String getWebPageSource(String url) throws IOException {
        URL u;
        InputStream is = null;
        BufferedReader br;
        String line;
        StringBuilder sb = new StringBuilder();
        
        try {
            u = new URL(url);
            is = u.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            
            return sb.toString();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
//        return JSoup.connect(url).get().html();
//        return "status = ok\nuid = 1\nhash = 00fda0g0ggrg0grds00srg45\nkey=1239546\n";
    }
}
