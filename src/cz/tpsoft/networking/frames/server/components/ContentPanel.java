/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.networking.frames.server.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author tomas.praslicak
 */
public class ContentPanel extends JPanel {
    private CommunicationList communicationList = new CommunicationList();
    private ClientsList clientsList = new ClientsList();

    public ContentPanel() {
        setLayout(new GridLayout(1, 2, 5, 5));
        setBorder(new EmptyBorder(0, 5, 5, 5));
        
        add(new JScrollPane(communicationList));
        add(new JScrollPane(clientsList));
    }

    public CommunicationList getCommunicationList() {
        return communicationList;
    }

    public ClientsList getClientsList() {
        return clientsList;
    }
}
