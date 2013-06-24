package cz.tpsoft.utils.components;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Okno, obsahujici metody, ktere ho v ramci monitoru vycentruji.
 * @author Tomas Praslicak
 */
public class JCentralizableFrame extends JFrame {
    /**
     * Nastavi nove rozmery okna a pote ho vycentruje.
     * @param width nova sirka dialogu.
     * @param height nova vyska dialogu.
     * @see JCentralizableFrame#centralize() 
     */
    public void centralize(int width, int height) {
        setSize(width, height);
        centralize();
    }
    
    /**
     * Vycentruje okno v ramci DefaultToolkit podle aktualnich rozmeru.
     * @see Toolkit#getDefaultToolkit()
     */
    public void centralize() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - getWidth()) / 2;
        int y = (screen.height - getHeight()) / 2;
        setLocation(x, y);
    }
}
