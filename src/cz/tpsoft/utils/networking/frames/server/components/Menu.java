/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.networking.frames.server.components;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author tomas.praslicak
 */
public class Menu extends JMenuBar {
    private JMenu windowMenu = new JMenu("Okno");
    private JMenu clientMenu = new JMenu("Klient");
    
    private JMenuItem[] windowItems = {
        new JMenuItem("Ukončit program")
    };
    
    private JMenuItem[] clientItems = {
        new JMenuItem("Odpojit všechny")
    };

    public Menu() {
        add(windowMenu);
        add(clientMenu);
        
        for (JMenuItem item : windowItems) {
            windowMenu.add(item);
        }
        for (JMenuItem item : clientItems) {
            clientMenu.add(item);
        }
    }

    public JMenu getWindowMenu() {
        return windowMenu;
    }

    public JMenuItem[] getWindowItems() {
        return windowItems;
    }

    public JMenu getClientMenu() {
        return clientMenu;
    }

    public JMenuItem[] getClientItems() {
        return clientItems;
    }
    
    
}
