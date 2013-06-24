/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.networking.frames;

import cz.tpsoft.utils.layoutmanager.ComponentManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author tomas.praslicak
 */
public abstract class FilterServerFrame extends ServerFrame {
    private static class FilterDialog extends JDialog {
        private FilterServerFrame owner;
        
        private JPanel contentPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        private JPanel topPanel = new JPanel(new GridLayout(1, 2));
        private JPanel footerPanel = new JPanel(new GridLayout(1, 1));
        
        private JPanel ipSettingsFilterPanel;
        private JPanel passwordSettingsFilterPanel;
        
        private JCheckBox useIpFilterBox = new JCheckBox("Používat IP filtr");
        private JCheckBox usePasswordBox = new JCheckBox("Používat heslo");
        
        private JTextField ipField = new JTextField("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");
        private JPasswordField passwordField = new JPasswordField();
        private JLabel passwordStateField = new JLabel();
        private JButton okButton = new JButton("Nastavit");
        
        public FilterDialog(FilterServerFrame owner) {
            super(owner);
            setModal(true);
            
            this.owner = owner;
            
            topPanel.setOpaque(false);
            contentPanel.setOpaque(false);
            footerPanel.setOpaque(false);
            
            topPanel.add(useIpFilterBox);
            topPanel.add(usePasswordBox);
            
            ipSettingsFilterPanel = ComponentManager.getComponentsGrid(new JComponent[][]  {
                { new JLabel("Reg. výraz, který se musí shodovat s IP klienta"), ipField }
            }, new ComponentManager.Alignment[][] {
                { ComponentManager.Alignment.LEFT, ComponentManager.Alignment.LEFT }
            }, ComponentManager.Alignment.CORNER_LEFTTOP, 5, 5);
        
            passwordSettingsFilterPanel = ComponentManager.getComponentsGrid(new JComponent[][]  {
                { new JLabel("Heslo"), passwordField, passwordStateField },
            }, new ComponentManager.Alignment[][] {
                { ComponentManager.Alignment.LEFT, ComponentManager.Alignment.CENTER_STRETCHED }
            }, ComponentManager.Alignment.CORNER_LEFTTOP, 5, 5);
            
            contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            contentPanel.add(ComponentManager.getMergedComponents(new Component[] {
                ipSettingsFilterPanel, passwordSettingsFilterPanel
            }, ComponentManager.Alignment.TOP, 5));
            
            footerPanel.add(okButton);
            
            setLayout(new BorderLayout(5, 5));
            add(topPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(footerPanel, BorderLayout.SOUTH);
            
            useIpFilterBox.addItemListener(new ItemListener() {
                @Override public void itemStateChanged(ItemEvent e) {
                    FilterDialog.this.updateIpState();
                }
            });
            usePasswordBox.addItemListener(new ItemListener() {
                @Override public void itemStateChanged(ItemEvent e) {
                    FilterDialog.this.updatePasswordState();
                }
            });
            okButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    if (FilterDialog.this.validateData()) {
                        FilterDialog.this.updateParentData();
                        FilterDialog.this.setVisible(false);
                    }
                }
            });
            
            updateIpState();
            updatePasswordState();
        }
        
        private void updateIpState() {
            ipField.setEnabled(useIpFilterBox.isSelected());
        }
        
        private void updatePasswordState() {
            passwordField.setEnabled(usePasswordBox.isSelected());
            passwordStateField.setEnabled(usePasswordBox.isSelected());
        }
        
        private boolean validateData() {
            StringBuilder sb = new StringBuilder();
            
            if (useIpFilterBox.isSelected()) {
                if (ipField.getText().isEmpty()) {
                    sb.append("Neplatný IP filtr.").append("\n");
                }
            }
            
            if (usePasswordBox.isSelected()) {
                if (passwordField.getPassword().length < 3) {
                    sb.append("Příliš krátké heslo.").append("\n");
                }
            }
            
            if (sb.length() == 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, sb.toString(),
                        "Chybná data", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        private void updateParentData() {
            if (useIpFilterBox.isEnabled()) {
                owner.ipFilter = ipField.getText();
            } else {
                owner.ipFilter = null;
            }
            if (usePasswordBox.isEnabled()) {
                owner.password = "";
                for (int a=0; a<passwordField.getPassword().length; a++) {
                    owner.password += passwordField.getPassword()[a];
                }
            } else {
                owner.password = null;
            }
        }
    }

    private FilterDialog filterDialog = new FilterDialog(this);
    private String password = null;
    private String ipFilter = null;
    
    public FilterServerFrame(String title, int port) throws IOException,
            HeadlessException {
        super(title, port);
    }
    
    public void showFilterDialog() {
        filterDialog.setVisible(true);
    }

    @Override
    protected boolean serverAcceptClient(Socket socket) {
        return socket.getInetAddress().getHostAddress().matches(ipFilter);
    }
}
