/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.networking.frames.server.components;

import cz.tpsoft.networking.NetServer;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author tomas.praslicak
 */
public class ClientsList extends JList<NetServer.Client> {
    private DefaultListModel<NetServer.Client> model = new DefaultListModel<>();

    public ClientsList() {
        setModel(model);
    }
    
    public void refresh(LinkedList<NetServer.Client> items) {
        model.removeAllElements();
        for (NetServer.Client item : items) {
            System.out.println("adding " + item);
            model.addElement(item);
        }
    }
}
