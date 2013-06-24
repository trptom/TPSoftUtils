package cz.tpsoft.utils.timer;

import java.util.LinkedList;

/**
 * Timer, do ktereho se daji pridavat listenery (stejny princip jako u swing
 * komponent).
 * @author Tomas Praslicak
 */
public class ListenerTimer extends Timer {
    private LinkedList<TimeoutListener> listeners = new LinkedList<>();

    public ListenerTimer(int timeout, boolean repeat) {
        super(timeout, repeat);
    }

    public ListenerTimer(int timeout, int repeatCount) {
        super(timeout, repeatCount);
    }
    
    /**
     * Prepsana akce predka, ktera zavola onTimeout vsech listeneru. Neni
     * synchronizovana, synchronizaci je nutne zaridit u potomku!
     * @param timeoutId id timeoutu.
     * @deprecated tato metoda je volana automaticky. Jeji manualni volani
     * z kodu muze narusit beh programu.
     * @see TimeoutListener#onTimeout(int) 
     */
    @Deprecated
    @Override
    public final void onTimeoutAction(int timeoutId) {
        for (TimeoutListener listener : listeners) {
            listener.onTimeout(timeoutId);
        }
    }
    
    public final void addTimeoutListener(TimeoutListener listener) {
        listeners.addLast(listener);
    }
    
    public final boolean removeTimeoutListener(TimeoutListener listener) {
        return listeners.remove(listener);
    }
    
    public final void clearTimeoutListeners() {
        listeners.clear();
    }
}
