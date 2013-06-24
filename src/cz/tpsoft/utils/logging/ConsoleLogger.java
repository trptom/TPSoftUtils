package cz.tpsoft.utils.logging;

import java.util.Calendar;

/**
 * Jednoducha trida, ktera usnadnuje logovani do konzole (log level, type,
 * datetime), ...
 * @author Tomas Praslicak
 */
public class ConsoleLogger {
    public static enum Type {
        ERROR(0, 1),
        INFO(1, 2),
        WARNING(2, 2),
        DEBUG(3, 3);
        
        private int number;
        private int defaultLevel;

        public int getNumber() {
            return number;
        }

        public int getDefaultLevel() {
            return defaultLevel;
        }

        private Type(int number, int defaultLevel) {
            this.number = number;
            this.defaultLevel = defaultLevel;
        }
    }
    
    private int logLevel = -1;
    
    public void log(String message, Type type) {
        log(message, type, type.getDefaultLevel());
    }
    
    public void log(String message, Type type, int level) {
        if (logLevel < 0 || logLevel > level) {
            System.out.println(getDateFormat() + ": <" + type.toString() + "> " + message);
        }
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }
    
    public String getDateFormat() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR)).append("-");
        sb.append(calendar.get(Calendar.MONTH) >= 9 ?
                calendar.get(Calendar.MONTH)+1 :
                "0" + (calendar.get(Calendar.MONTH)+1)).append("-");
        sb.append(calendar.get(Calendar.DATE) >= 10 ?
                calendar.get(Calendar.DATE) :
                "0" + (calendar.get(Calendar.DATE))).append(" ");
        sb.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
        sb.append(calendar.get(Calendar.MINUTE) >= 10 ?
                calendar.get(Calendar.MINUTE) :
                "0" + (calendar.get(Calendar.MINUTE))).append(":");
        sb.append(calendar.get(Calendar.SECOND) >= 10 ?
                calendar.get(Calendar.SECOND) :
                "0" + (calendar.get(Calendar.SECOND)));
        return sb.toString();
    }
}
