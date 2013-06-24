package cz.tpsoft.utils.networking;

import java.io.Serializable;

/**
 * Trida posilane zpravy.
 * @param <DataType> typ posilanych dat.
 * @author Tomas Praslicak
 */
public class Message<DataType extends Serializable> implements Serializable {
    public static final Message DISCONNECT = new Message(0, null) {
        @Override public String toString() {
            return "Message[DISCONNECT]";
        }
    };
    
    public static final Message ACCESS_DENIED = new Message(0, null) {
        @Override public String toString() {
            return "Message[ACCESS_DENIED]";
        }
    };
    
    private long message;
    private DataType data;

    public Message(long message, DataType data) {
        this.message = message;
        this.data = data;
    }
    
    public long getMessage() {
        return message;
    }

    public DataType getData() {
        return data;
    }

    @Override
    public int hashCode() {
        return (int)message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message tmp = (Message)obj;
            if (tmp.message == message) {
                if ((tmp.data == null && data == null) ||
                        (tmp.data != null && tmp.data.equals(data))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Message[" + message + (data != null ? (" | " + data.toString()) : "") + "]";
    }
    
    public boolean equalsIgnoreData(Object obj) {
        if (obj instanceof Message) {
            Message tmp = (Message)obj;
            if (tmp.message == message) {
                return true;
            }
        }
        return false;
    }
}
