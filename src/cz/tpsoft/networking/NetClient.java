package cz.tpsoft.networking;

import cz.tpsoft.logging.ConsoleLogger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * 
 * @author Tomas Praslicak
 */
public abstract class NetClient {
    private class Listener extends Thread {
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        @Override
        public void run() {
            enabled = true;
            while (enabled) {
                try {
                    Object o = NetClient.this.inputStream.readObject();
                    if (!enabled) {
                        NetClient.this.logger.log(
                                "object received but listening from server disabled",
                                ConsoleLogger.Type.INFO);
                        return;
                    }
                    
                    if (o instanceof Message) {
                        if (o.equals(Message.DISCONNECT)) {
                            NetClient.this.disconnect();
                        }
                        NetClient.this.messageReceived((Message)o);
                    } else {
                        NetClient.this.logger.log(
                                "non message type received from server",
                                ConsoleLogger.Type.WARNING);
                    }
                } catch (ClassNotFoundException | IOException ex) {
                    if (!enabled && ex instanceof SocketException &&
                            ex.getMessage().equalsIgnoreCase("socket closed")) {
                        NetClient.this.logger.log(
                                "listening from server stopped",
                                ConsoleLogger.Type.INFO);
                        return;
                    } else {
                        NetClient.this.logger.log(
                                "error during reading object from server",
                                ConsoleLogger.Type.ERROR);
                        ex.printStackTrace(System.err);
                    }
                }
            }
        }
    }
    
    private Socket socket;
    private ConsoleLogger logger = new ConsoleLogger();
    private NetClient.Listener listener = new NetClient.Listener();
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }
    
    public synchronized void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            listener.start();
            afterConnect(true);
        } catch (IOException ex) {
            logger.log(
                    "exception during creating socket",
                    ConsoleLogger.Type.ERROR);
            ex.printStackTrace(System.err);
            afterConnect(false);
        }
    }
    
    public synchronized void disconnect() {
        listener.setEnabled(false);
        sendMessage(Message.DISCONNECT);
        logger.log(
                "disconnect message sent",
                ConsoleLogger.Type.DEBUG);
        try {
            inputStream.close();
        } catch (IOException ex) {
            logger.log(
                    "exception during closing input stream",
                    ConsoleLogger.Type.ERROR);
            ex.printStackTrace(System.err);
        }
        try {
            outputStream.close();
        } catch (IOException ex) {
            logger.log(
                    "exception during closing output stream",
                    ConsoleLogger.Type.ERROR);
            ex.printStackTrace(System.err);
        }
        try {
            socket.close();
        } catch (IOException ex) {
            logger.log(
                    "exception during closing socket",
                    ConsoleLogger.Type.ERROR);
            ex.printStackTrace(System.err);
        }
        afterDisconnect();
    }
    
    public synchronized void sendMessage(Message message) {
        try {
            getOutputStream().writeObject(message);
        } catch (IOException ex) {
            logger.log(
                    "exception during sending message to server",
                    ConsoleLogger.Type.ERROR);
            ex.printStackTrace(System.err);
        }
    }
    
    public synchronized void messageReceived(Message message) {}    
    public abstract void afterConnect(boolean success);
    public abstract void afterDisconnect();
}
