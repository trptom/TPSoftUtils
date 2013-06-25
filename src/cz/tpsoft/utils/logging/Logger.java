package cz.tpsoft.utils.logging;

import java.io.*;
import java.util.Calendar;

/**
 * Trida, vytvorena pro jednoduche logovani zprav do souboru.
 * @author Tomas Praslicak
 * @version 1.0
 */
public class Logger {
    public static final String TYPE_ERROR = "ERROR";
    public static final String TYPE_INFO = "INFO";
    public static final String TYPE_WARNING = "WARNING";
    public static final String TYPE_DEBUG = "DEBUG";
    private static String outputFile = null;
    public static boolean enabled = true;
    public static boolean logDebug = true;
    
    /**
     * @return true, pokud je nastaven vystupni soubor, kam se ma logovat,
     * jinak false.
     * @see #setOutputFile(java.lang.String) 
     */
    public static boolean isTurnedOn() {
        return outputFile != null;
    }
    
    /**
     * Nastavi vystupni soubor. Defaultne je null, takze dokud nebude nastaven,
     * nebude probihat logovani (funkce log() bude vracet false).
     * @param file vystupni soubor.
     * @see #isTurnedOn() 
     */
    public static void setOutputFile(String file) {
        outputFile = file;
    }
    
    private static String generateLog(String text, String type) {
        StringBuilder ret = new StringBuilder("");
        Calendar c = Calendar.getInstance();
        ret.append(c.get(Calendar.YEAR)).append("-");
        if (c.get(Calendar.MONTH) < 10) ret.append("0");
        ret.append(c.get(Calendar.MONTH)).append("-");
        if (c.get(Calendar.DATE) < 10) ret.append("0");
        ret.append(c.get(Calendar.DATE)).append(" ");
        if (c.get(Calendar.HOUR_OF_DAY) < 10) ret.append("0");
        ret.append(c.get(Calendar.HOUR_OF_DAY)).append(":");
        if (c.get(Calendar.MINUTE) < 10) ret.append("0");
        ret.append(c.get(Calendar.MINUTE)).append(":");
        if (c.get(Calendar.SECOND) < 10) ret.append("0");
        ret.append(c.get(Calendar.SECOND)).append(" <");
        ret.append(type).append(">: ");
        ret.append(text).append("\n");
        return ret.toString();
    }
    
    private static FileWriter getFileWriter() throws IOException {
        File file = new File(outputFile);
        if (!file.exists()) {
            file.createNewFile();
        }
        return new FileWriter(file, true);
    }
    
    /**
     * Zaloguje zpravu do aktualniho logu. Musi byt nastaven vystupni soubor
     * a zaroven povoleno logovani.
     * @param text text zpravy.
     * @param type typ zpravy (to co bude zobrazeno v lomenych zavorkach). Je
     * doporuceno pouzit konstanty teto tridy.
     * @return true, pokud byla zprava zalogovana, jinak false.
     * @see #isTurnedOn() 
     * @see #isEnabled() 
     * @see #setOutputFile(java.lang.String) 
     */
    public static boolean log(String text, String type) {
        if (type != TYPE_DEBUG || logDebug) {
            if (isTurnedOn() && isEnabled()) {
                BufferedWriter bw = null;
                FileWriter fw;
                try {
                    fw = getFileWriter();
                    bw = new BufferedWriter(fw);
                    bw.append(generateLog(text, type));
                    return true;
                } catch (IOException ex) {
                    ex.printStackTrace(System.out);
                    return false;
                } finally {
                    if (bw != null) {
                        try {
                            bw.close();
                        }catch (IOException ex) {
                            ex.printStackTrace(System.out);
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Nastavi, zda je logovani povoleno.
     * @param enabled true = logovani povoleno, false = logovani zakazano.
     * @see #enableLogging() 
     * @see #disableLogging() 
     * @see #isEnabled() 
     */
    public static void setEnabled(boolean enabled) {
        Logger.enabled = enabled;
    }
    
    /**
     * Povoli logovani.
     */
    public static void enableLogging() {
        setEnabled(true);
    }
    
    /**
     * Zakaze logovani.
     */
    public static void disableLogging() {
        setEnabled(false);
    }

    /**
     * @return true, pokud je logovani povoleno, jinak false.
     */
    public static boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Zaloguje do vystupniho souboru printStackTrace() vyjimky.
     * @param exception vyjimka, urcena k logovani.
     * @return true, pokud se logovani povedlo, jinak false.
     */
    public static boolean log(Exception exception) {
        BufferedWriter bw = null;
        if (isTurnedOn() && isEnabled()) {
            try {
                bw = new BufferedWriter(getFileWriter());
                Logger.log(exception.toString(), TYPE_ERROR);
                for (StackTraceElement element : exception.getStackTrace()) {
                    Logger.log("at " + element.toString(), TYPE_ERROR);
                }
                return true;
            } catch (IOException ex) {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException ex2) {}
                }
                ex.printStackTrace(System.out);
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * Konstruktor zakazan.
     */
    private Logger() {}
}
