/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.components;

import java.awt.Dimension;

/**
 *
 * @author tomas.praslicak
 */
public class JMaximizedFrame extends JCentralizableFrame {
    public static final Dimension NOT_MAXIMIZED_SIZE = new Dimension(800, 600);

    public JMaximizedFrame() {
        this(NOT_MAXIMIZED_SIZE);
    }
    
    public JMaximizedFrame(Dimension notMaximizedSize) {
        // Pri zruseni maximalizace bude nastavena tato velikost
        centralize(notMaximizedSize.width, notMaximizedSize.height);
        // Maximalizace okna
        setExtendedState(getExtendedState()|MAXIMIZED_BOTH);
    }
}
