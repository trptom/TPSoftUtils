/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.localization.component;

import cz.tpsoft.utils.localization.Messages;
import javax.swing.JMenuItem;

/**
 *
 * @author tomas.praslicak
 */
public class LocalizedMenuItem extends JMenuItem implements LocalizedComponent {
    private String message;
    private boolean messageEnabled = true;

    public LocalizedMenuItem(String message) {
        setMessage(message);
    }

    @Override
    public final void localize() {
        if (messageEnabled) {
            setText(Messages.getMessage(getMessage()));
        } else {
            setText(getMessage());
        }
    }
    
    public String getMessage() {
        return message;
    }

    public final void setMessage(String message) {
        this.message = message;
        localize();
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
