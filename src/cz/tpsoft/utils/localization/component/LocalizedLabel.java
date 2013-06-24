/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.localization.component;

import javax.swing.JLabel;
import cz.tpsoft.utils.localization.Messages;

/**
 *
 * @author tomas.praslicak
 */
public class LocalizedLabel extends JLabel implements LocalizedComponent {
    private String message;
    private boolean messageEnabled = true;

    public LocalizedLabel(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public final void setMessage(String message) {
        this.message = message;
        localize();
    }

    @Override
    public final void localize() {
        if (messageEnabled) {
            setText(Messages.getMessage(getMessage()));
        } else {
            setText(getMessage());
        }
    }

    public boolean isMessageEnabled() {
        return messageEnabled;
    }

    public void setMessageEnabled(boolean messageEnabled) {
        this.messageEnabled = messageEnabled;
    }
    
    public void disableMessage() {
        setMessageEnabled(false);
    }
    
    public void enableMessage() {
        setMessageEnabled(true);
    }
}
