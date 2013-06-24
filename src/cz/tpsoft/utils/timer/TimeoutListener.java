package cz.tpsoft.utils.timer;

/**
 * Zakladni akce pro ListenerTimer. Ma metodu onTimeout, ktera je v timeru
 * volana pri dosazeni timeoutu.
 * @author Tomas Praslicak
 * @see ListenerTimer
 */
public interface TimeoutListener {
    /**
     * Metoda, volana pri dosazeni timeoutu.
     * @param timeoutId poradove cislo timeoutu (kolikrat byl dosazen), pocinaje
     * nulou.
     */
    public void onTimeout(int timeoutId);
}
