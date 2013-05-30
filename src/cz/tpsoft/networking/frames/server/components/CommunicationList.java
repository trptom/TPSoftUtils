/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.networking.frames.server.components;

import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author tomas.praslicak
 */
public class CommunicationList extends JList<String> {
    private DefaultListModel<String> model = new DefaultListModel<>();

    public CommunicationList() {
        setModel(model);
    }
    
    public void refresh(LinkedList<String> items) {
        model.removeAllElements();
        for (String item : items) {
            model.addElement(item);
        }
    }
}
