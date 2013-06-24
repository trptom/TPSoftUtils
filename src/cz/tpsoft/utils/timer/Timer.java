package cz.tpsoft.utils.timer;

/**
 * Trida simulujici casovac. Pocka urcitou dobu a pak zavola metodu
 * onTimeoutAction. Volani muze probehnout i opakovane (i nekonecna smycka),
 * vzdy s danou casovu prodlevou.<br />
 * Spousti se pres metodu vlakna start.
 * @author Tomas Praslicak
 * @see Thread#start() 
 * @see #onTimeoutAction(int) 
 */
public abstract class Timer extends Thread {
    private int repeatCount, timeout;
    
    /**
     * Konstruktor timeru.
     * @param timeout kolik casu (ms) musi uplynout do zavolani onTimeoutAction
     * (&gt 0).
     * @param repeat zda se bude volani onTimeoutAction (true), nebo ne (false).
     * @see #onTimeoutAction(int) 
     */
    public Timer(int timeout, boolean repeat) {
        if (timeout < 1) {
            throw new IllegalArgumentException("timeout musi byt >= 1");
        }
        this.timeout = timeout;
        this.repeatCount = repeat ? -1 : 1;
    }
    
    /**
     * Konstruktor timeru.
     * @param timeout kolik casu (ms) musi uplynout do zavolani onTimeoutAction
     * (&gt= 0).
     * @param repeatCount kolikrat se bude opakovat volani (vzdy jednou mezi
     * timeouty; &gt 0).
     */
    public Timer(int timeout, int repeatCount) {
        if (repeatCount < 1) {
            throw new IllegalArgumentException("repeatcount musi byt >= 1");
        }
        if (timeout < 1) {
            throw new IllegalArgumentException("timeout musi byt >= 1");
        }
        this.timeout = timeout;
        this.repeatCount = repeatCount;
    }
    
    /**
     * Akce, volana pri dosazeni timeoutu.
     * @param timeoutId poradove id timeoutu, zacinajici 0 (tzn. kolikrat byl
     * naplnen timeout - 1).
     */
    public abstract void onTimeoutAction(int timeoutId);

    @Override
    public final void run() {
        int id = 0;
        while (repeatCount != 0) {
            try {
                sleep(timeout);
            } catch (InterruptedException ex) {
                return;
            }
            onTimeoutAction(id);
            id++;
            if (repeatCount > 0) {
                repeatCount--;
            }
        }
    }
}
