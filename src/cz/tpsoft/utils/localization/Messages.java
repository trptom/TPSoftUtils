package cz.tpsoft.utils.localization;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JFrame;
import cz.tpsoft.utils.localization.component.LocalizedComponent;

/**
 * Trida, slouzici k lokalizaci aplikace. Nacita property soubory, automaticky
 * vybira defaultni jazyk, kontroluje dostupnost jazykovych mutaci, ...<br />
 * Doporucuji zacit metodami <i>loadAvaliableLanguages</i>
 * nebo <i>loadLanguage</i>.<br /><br />
 * Format propertyfiles:
 * <li>klic = hodnota (na jednom radku)</li>
 * <li>hodnota muze obsahovat specialni hodnoty, doplnovane az za behu programu,
 * ty se davaji do tagu &lt%id specialni%&gt, kde id je id v poli stringu,
 * dodavanych prislusne metode (tj. getMessage s parametrem special).</li><br />
 * Priklad:
 * <li>Message: pokus = Toto je &lt%0&gt cislo &lt%01%&gt </li>
 * <li>Metoda: getMessage("pokus", new String[] {"pokus", "1"})</li>
 * <li>Vystup: Toto je <b>pokus</b> cislo <b>1</b> </li>
 * @author Tomas Praslicak
 * @see #loadAvaliableLanguages(java.lang.String) 
 * @see #loadLanguage(java.lang.String, java.util.Locale) 
 * @see #loadLanguage(java.util.Locale, java.lang.String, java.lang.String) 
 * @see LocalizedComponent
 */
public class Messages {
    private static LinkedList<Locale> supportedLanguages = new LinkedList<>();
    private static Locale defaultLanguage = null;
    private static HashMap<String, Properties> loadedFiles = new HashMap<>();
    private static HashMap<Locale, Properties> map = new HashMap<>();

    public static Locale getDefaultLanguage() {
        return defaultLanguage;
    }
    
    /**
     * Nastavi vychozi jazyk podle jazyka, nastaveneho v OS. Pokud neni takovy
     * jazyk podporovan, nastavi prvni hodnotu z supportedLanguages.
     * @see #supportedLanguages
     */
    private static void setDefaultLanguage() {
        if (supportedLanguages.contains(Locale.getDefault())) {
            defaultLanguage = Locale.getDefault();
        } else {
            defaultLanguage = supportedLanguages.getFirst();
        }
    }
    
    /**
     * Nacte vsechny dostupne jazyky ve slozce.
     * @param propertyFilesFolder slozka, ve ktere se budou hledat dostupne
     * jazyky.
     */
    public static void loadAvaliableLanguages(String propertyFilesFolder) {
        map.clear();
        supportedLanguages.clear();
        loadedFiles.clear();
        defaultLanguage = null;
        
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (!locale.getCountry().isEmpty()) {
                loadLanguage(propertyFilesFolder, locale);
            }
        }
        
        setDefaultLanguage();
    }
    
    /**
     * Nacte jazyk pro dane Locale
     * @param propertyFilesFolder slozka, ze ktere se ma nacitat
     * messages_<locale>.properties.
     * @param locale locale, jejihz zkratka bude za messages.
     * @return true, pokud bylo nacitani uspesne, jinak false.
     */
    public static boolean loadLanguage(String propertyFilesFolder, Locale locale) {
        // Smazani lomitka na konci.
        propertyFilesFolder.replaceAll("[\\/\\\\]$", "");
        
        boolean ret = loadLanguage(locale, locale.toLanguageTag(),
                propertyFilesFolder + "/messages_"
                + locale.toLanguageTag() + ".properties");
        if (!ret) {
            ret = loadLanguage(locale, locale.getLanguage(),
                    propertyFilesFolder + "/messages_"
                    + locale.getLanguage() + ".properties");
        }
        return ret;
    }
    
    public static boolean loadLanguage(Locale locale, String fileLang, String file) {
        if (loadedFiles.get(fileLang) != null) {
            supportedLanguages.addLast(locale);
            map.put(locale, loadedFiles.get(fileLang));
            return true;
        }
        
        File inFile = new File(file);
        if (!inFile.exists()) {
            return false;
        }

        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream(inFile));
            loadedFiles.put(fileLang, prop);
        } catch (IOException ex) {}
        
        supportedLanguages.addLast(locale);
        map.put(locale, prop);

        return true;
    }

    public static String getMessage(String key) {
        return getMessage(key, getDefaultLanguage(), null);
    }
    
    public static String getMessage(String key, String[] special) {
        return getMessage(key, getDefaultLanguage(), special);
    }

    public static String getMessage(String key, Locale locale) {
        return getMessage(key, locale, null);
    }
    
    public static String getMessage(String key, Locale locale, String[] special) {
        if (locale == null) {
            return key;
        }
        
        Properties properties = map.get(locale);
        if (properties == null) {
            return key;
        }
        String value = properties.getProperty(key, key);
        
        // Pokud nejsou uvedeny specialni hodnoty, neprobehne skladani retezce.
        if (special == null || special.length == 0) {
            return value;
        }
        
        String[] splitted = value.split("<%[\\d]+%>");
        
        // Pokud v retezci nejsou zadne znaky pro specialni hodnoty <%>, taky
        // neprobehne skladani.
        if (splitted.length == 1) {
            return value;
        }
        
        // Zparsuji value tak, abych na konci mel jen stringy klicu.
        for (String part : splitted) {
            value = value.replace(part, "");
        }
        value = value.replaceFirst("^<%", "");
        value = value.replaceFirst("%>$", "");
        String[] numbers = value.split("%><%");
        
        // Skladani retezce se specialnimi hodnotami.
        StringBuilder sb = new StringBuilder("");
        for (int a=0; a<splitted.length; a++) {
            sb.append(splitted[a]);
            if (a < numbers.length) {
                try {
                    sb.append(special[Integer.parseInt(numbers[a])]);
                } catch ( NumberFormatException |
                        IndexOutOfBoundsException ex) {}
            }
        }
        return sb.toString();
    }
    
    /**
     * Nastavi jazykovou verzi a lokalizuje vsechny komponenty v kontejneru.
     * @param locale novy defaultni jazyk. Musi byt podporovan (obsazen v
     * supportedLanguages).
     * @return true, pokud byl jazyk nastaven na locale, jinak false
     * (=nepodporovany jazyk).
     * @see #getSupportedLanguages() 
     */
    public static boolean setDefaultLanguage(Locale locale) {
        if (locale != defaultLanguage) {
            if (!supportedLanguages.contains(locale)) {
                return false;
            }
            defaultLanguage = locale;
        }
        return true;
    }

    /**
     * @return novou instanci seznamu podporovanych jazyku (tzn. zmena tohoto
     * seznamu nema na Messages vliv).
     */
    public static LinkedList<Locale> getSupportedLanguages() {
        return new LinkedList<>(supportedLanguages);
    }
    
    /**
     * Spusti lokalizaci u vybraneho framu.
     * @param frame frame, ktery se ma lokalizovat.
     */
    public static void localize(JFrame frame) {
        localize(frame.getContentPane().getComponents());
    }
    
    /**
     * Spusti lokalizaci u vybrane komponenty.
     * @param component komponenta, ktera se ma lokalizovat.
     */
    public static void localize(Component component) {
        localize(new Component[] {component});
    }
    
    /**
     * Spusti lokalizaci u vybranych komponent.
     * @param components komponenty, u kterych ma probehnout (rekurzivni)
     * lokalizace.
     */
    public static void localize(Component[] components) {
        for (Component item : components) {
            if (item instanceof JComponent) {
                localize(((JComponent)item).getComponents());
            }
            if (item instanceof LocalizedComponent) {
                ((LocalizedComponent)item).localize();
            }
        }
    }
}
