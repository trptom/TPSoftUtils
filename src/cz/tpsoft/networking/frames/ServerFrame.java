package cz.tpsoft.networking.frames;

import cz.tpsoft.networking.Message;
import cz.tpsoft.networking.NetServer;
import cz.tpsoft.networking.frames.server.components.ContentPanel;
import cz.tpsoft.networking.frames.server.components.Menu;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * Okno serverove casti aplikace.
 * @author Tomas Praslicak
 */
public class ServerFrame extends JFrame {
    private class DefaultWindowListener implements WindowListener {
        @Override
        public void windowClosing(WindowEvent e) {
            if (ServerFrame.this.getServer().getClients().size() > 0) {
                ServerFrame.this.showUnableCloseDialog();
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            } else {
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }

        @Override public void windowOpened(WindowEvent e) {}
        @Override public void windowClosed(WindowEvent e) {}
        @Override public void windowIconified(WindowEvent e) {}
        @Override public void windowDeiconified(WindowEvent e) {}
        @Override public void windowActivated(WindowEvent e) {}
        @Override public void windowDeactivated(WindowEvent e) {}
    }
    
    private LinkedList<String> communacationMessages = new LinkedList<>();
    private JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
    
    private ContentPanel content = null;
    private Menu menu = null;
    
    private NetServer server;

    public ServerFrame(String title, int port) throws IOException, HeadlessException {
        super(title);
        setLayout(new GridLayout(1, 1));
        addWindowListener(new DefaultWindowListener());
        
        add(contentPanel);

        server = new NetServer(port) {
            @Override
            protected boolean acceptClient(Socket socket) {
                return ServerFrame.this.serverAcceptClient(socket);
            }

            @Override
            protected void clientAccepted(NetServer.Client client) {
                ServerFrame.this.refreshClients();
                ServerFrame.this.serverClientAccepted(client);
            }

            @Override
            protected void clientDisconnected(NetServer.Client client) {
                ServerFrame.this.refreshClients();
                ServerFrame.this.serverClientDisconnected(client);
            }

            @Override
            protected void messageReceived(NetServer.Client client,
                    Message message) {
                getCommunacationMessages().addFirst("<- ["
                        + client.toString() + "] " + message.toString());
                if (getCommunacationMessages().size() > 100) {
                    getCommunacationMessages().removeLast();
                }
                ServerFrame.this.refreshCommunication();
                ServerFrame.this.serverMessageReceived(client, message);
            }

            @Override
            protected void messageSent(NetServer.Client client,
                    Message message) {
                getCommunacationMessages().addFirst("-> ["
                        + client.toString() + "] " + message.toString());
                if (getCommunacationMessages().size() > 100) {
                    getCommunacationMessages().removeLast();
                }
                ServerFrame.this.refreshCommunication();
                ServerFrame.this.serverMessageSent(client, message);
            }
        };
    }
    
    protected final void constructDefaultStructure() {
        content = new ContentPanel();
        menu = new Menu();
        
        getContentPanel().add(menu, BorderLayout.NORTH);
        getContentPanel().add(content, BorderLayout.CENTER);
        
        menu.getClientItems()[0].addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                ServerFrame.this.server.disconnectAllClients();
            }
        });
        menu.getWindowItems()[0].addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (server.getClients().size() > 0) {
                    showUnableCloseDialog();
                } else {
                    System.exit(0);
                }
            }
        });
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
    
    public NetServer getServer() {
        return server;
    }
    
    public final void refreshClients() {
        if (content != null) {
            content.getClientsList().refresh(server.getClients());
        }
    }
    
    public final void refreshCommunication() {
        if (content != null) {
            content.getCommunicationList().refresh(getCommunacationMessages());
        }
    }
    
    public LinkedList<String>getCommunacationMessages() {
        return communacationMessages;
    }
    
    private void showUnableCloseDialog() {
        JOptionPane.showMessageDialog(this,
                "Okno nelze zavřít, pravděpodobně nebyli odpojeni všichni klienti.",
                "",
                JOptionPane.ERROR_MESSAGE);
    }
    
    protected boolean serverAcceptClient(Socket socket) { return true; }
    protected void serverClientAccepted(NetServer.Client client) {}
    protected void serverClientDisconnected(NetServer.Client client) {}
    protected void serverMessageReceived(NetServer.Client client, Message message) {}
    protected void serverMessageSent(NetServer.Client client, Message message) {}
}
