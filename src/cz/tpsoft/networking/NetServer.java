/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.networking;

import cz.tpsoft.logging.ConsoleLogger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

/**
 *
 * @author tomas.praslicak
 */
public abstract class NetServer {
    public class Client {
        public class Listener extends Thread {
            private boolean enabled;

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public boolean isEnabled() {
                return enabled;
            }

            @Override
            public void run() {
                enabled = true;
                while (enabled) {
                    try {
                        Object o = Client.this.inputStream.readObject();
                        if (!enabled) {
                            NetServer.this.logger.log(
                                    "object from client " + Client.this.getIp()
                                    + " received but listening disabled",
                                    ConsoleLogger.Type.INFO);
                            return;
                        }
                        
                        if (o instanceof Message) {
                            NetServer.this.logger.log(
                                    "received " + ((Message)o).toString() + " from "
                                    + Client.this.toString(),
                                    ConsoleLogger.Type.DEBUG);
                            if (o.equals(Message.DISCONNECT)) {
                                NetServer.this.disconnectClient(Client.this);
                            }
                            NetServer.this.messageReceived(Client.this, (Message)o);
                        } else {
                            NetServer.this.logger.log(
                                    "non message type received from "
                                    + Client.this.toString(),
                                    ConsoleLogger.Type.WARNING);
                        }
                    } catch (ClassNotFoundException | IOException ex) {
                        if (!enabled && ex instanceof SocketException &&
                                ex.getMessage().equalsIgnoreCase("socket closed")) {
                            NetServer.this.logger.log(
                                    "listening from "
                                    + Client.this.toString()
                                    + " stopped",
                                    ConsoleLogger.Type.INFO);
                            return;
                        } else {
                                NetServer.this.logger.log(
                                        "error during reading object from "
                                        + Client.this.getIp(),
                                        ConsoleLogger.Type.ERROR);
                                ex.printStackTrace(System.err);
                        }
                    }
                }
            }
        }
        
        private Socket socket;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;
        private Listener listener = new Listener();

        public Client(Socket socket) throws IOException {
            this.socket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }
        
        public void startListening() {
            this.listener.start();
        }
        
        public void stopListening() {
            this.listener.setEnabled(false);
        }

        public Socket getSocket() {
            return socket;
        }

        public boolean isConnected() {
            return !getSocket().isClosed();
        }

        public ObjectOutputStream getOutputStream() {
            return outputStream;
        }

        public ObjectInputStream getInputStream() {
            return inputStream;
        }
        
        public void close() {
            String ip = getIp();
            try {
                outputStream.close();
            } catch (IOException ex) {
                NetServer.this.logger.log(
                        "exception during closing outputStream of " + toString(),
                        ConsoleLogger.Type.ERROR);
                ex.printStackTrace(System.err);
            }
            try {
                inputStream.close();
            } catch (IOException ex) {
                NetServer.this.logger.log(
                        "exception during closing inputStream of " + toString(),
                        ConsoleLogger.Type.ERROR);
                ex.printStackTrace(System.err);
            }
            try {
                socket.close();
            } catch (IOException ex) {
                NetServer.this.logger.log(
                        "exception during closing socket of " + toString(),
                        ConsoleLogger.Type.ERROR);
                ex.printStackTrace(System.err);
            }
        }
        
        public String getIp() {
            return socket.getInetAddress().getHostAddress();
        }

        @Override
        public String toString() {
            return "NetServer.Client@" + getIp();
        }
    }
    
    private class Listener extends Thread {
        private boolean enabled;

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }
        
        @Override
        public void run() {
            NetServer.this.logger.log(
                    "listening on port " + NetServer.this.socket.getLocalPort()
                    + " started",
                    ConsoleLogger.Type.DEBUG);
            enabled = true;
            while (enabled) {
                try {
                    Socket socket = NetServer.this.socket.accept();
                    synchronized (NetServer.this) {
                        if (!enabled) { // pokud nemam povoleno pridavani
                            NetServer.this.logger.log(
                                    "client from "
                                    + socket.getInetAddress().getHostAddress()
                                    + " readed, but adding disabled ... skipping",
                                    ConsoleLogger.Type.INFO);
                            return;
                        }

                        if (NetServer.this.acceptClient(socket)) {
                            NetServer.this.logger.log(
                                    "client from "
                                    + socket.getInetAddress().getHostAddress()
                                    + " accepted",
                                    ConsoleLogger.Type.INFO);
                            NetServer.this.clients.addLast(new Client(socket));
                            NetServer.this.clients.getLast().startListening();
                            NetServer.this.logger.log(
                                    "current clients count: "
                                    + NetServer.this.clients.size(),
                                    ConsoleLogger.Type.DEBUG);
                            NetServer.this.clientAccepted(NetServer.this.clients.getLast());
                        } else {
                            NetServer.this.logger.log(
                                    "client from "
                                    + socket.getInetAddress().getHostAddress()
                                    + " not accepted",
                                    ConsoleLogger.Type.INFO);
                        }
                    }
                } catch (IOException ex) {
                    NetServer.this.logger.log(
                            "exception during accepting",
                            ConsoleLogger.Type.ERROR);
                    ex.printStackTrace(System.err);
                }
            }
        }
    }
    
    private int port;
    private LinkedList<Client> clients = new LinkedList<>();
    private ServerSocket socket;
    private ConsoleLogger logger = new ConsoleLogger();
    private Listener listener = new Listener();

    public NetServer(int port) throws IOException {
        this.port = port;
        this.socket = new ServerSocket(port);
    }
    
    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            logger.log(
                    "exception during closing server socket",
                    ConsoleLogger.Type.ERROR);
            ex.printStackTrace(System.err);
        }
    }
    
    public final void startListening() {
        listener.start();
    }
    
    public final void stopListening() {
        listener.setEnabled(false);
    }

    public int getPort() {
        return port;
    }

    public LinkedList<Client> getClients() {
        return clients;
    }

    public ConsoleLogger getLogger() {
        return logger;
    }
    
    public synchronized void sendToAll(Message message) {
        for (Client client : getClients()) {
            sendTo(client, message);
        }
    }
    
    public synchronized void sendTo(Client client, Message message) {
        if (client.isConnected()) {
            try {
                client.getOutputStream().writeObject(message);
                messageSent(client, message);
            } catch (IOException ex) {
                logger.log(
                        "exception during sending message ("
                        + message.toString() + ") to " + client.toString(),
                        ConsoleLogger.Type.ERROR);
                ex.printStackTrace(System.err);
            }
        } else {
            logger.log(
                    "attempt to send message ("
                    + message.toString() + ") to disconnected client ("
                    + client.toString() + ")",
                    ConsoleLogger.Type.ERROR);
        }
    }
    
    public synchronized void disconnectClient(Client client) {
        getClients().remove(client);
        client.stopListening();
        client.close();
        logger.log(
                client.toString() + " disconnected",
                ConsoleLogger.Type.ERROR);
        clientDisconnected(client);
    }
    
    public synchronized void disconnectAllClients() {
        for (Client client : getClients()) {
            sendTo(client, Message.DISCONNECT);
        }
    }
    
    protected abstract boolean acceptClient(Socket socket);
    
    protected abstract void clientAccepted(Client client);
    protected abstract void clientDisconnected(Client client);
    protected abstract void messageReceived(Client client, Message message);
    protected abstract void messageSent(Client client, Message message);
}
