/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.networking.frames;

import cz.tpsoft.networking.NetClient;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 *
 * @author tomas.praslicak
 */
public abstract class ClientFrame extends JFrame {
    private class DefaultWindowListener implements WindowListener {
        /**
         * Pri zavirani okna odpoji klienta, pokud je pripojen.
         * @param e udalost WindowEvent.
         */
        @Override
        public void windowClosing(WindowEvent e) {
            if (ClientFrame.this.client.isConnected()) {
                ClientFrame.this.client.disconnect();
            }
        }

        // nevyuzite akce, ktere je ale nutno implementovat
        @Override public void windowOpened(WindowEvent e) {}
        @Override public void windowClosed(WindowEvent e) {}
        @Override public void windowIconified(WindowEvent e) {}
        @Override public void windowDeiconified(WindowEvent e) {}
        @Override public void windowActivated(WindowEvent e) {}
        @Override public void windowDeactivated(WindowEvent e) {}
    }
    
    private NetClient client;

    public ClientFrame(String title) throws HeadlessException {
        super(title);
        setLayout(new GridLayout(1, 1));
        addWindowListener(new DefaultWindowListener());
        
        client = new NetClient() {
            @Override public void afterConnect(boolean success) {
                ClientFrame.this.clientAfterConnect(success);
            }
            @Override public void afterDisconnect() {
                ClientFrame.this.clientAfterDisconnect();
            }
        };
    }

    public NetClient getClient() {
        return client;
    }
    
    /**
     * Metoda, ktera je volana po pokusu o pripojeni. Sama o sobe nic necela,
     * je urcena k pretizeni.
     * @param success true, pokud bylo pripojeni uspesne, jinak false.
     */
    public void clientAfterConnect(boolean success) {}
    /**
     * Metoda, ktera je volana hned po pokusu o odpojeni klienta. Sama o sobe
     * nic necela, je urcena k pretizeni.
     */
    public void clientAfterDisconnect() {}
}
