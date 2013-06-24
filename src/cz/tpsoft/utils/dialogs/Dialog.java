package cz.tpsoft.utils.dialogs;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Trida, usnadnujici zobrazovani JOptionPane dialogu.<br />
 * Vsechny metody pouze zpracovavaji parametry a pote volaji metody tridy
 * JOptionPane.
 * @author Tomas Praslicak
 * @see JOptionPane
 */
public class Dialog {
    private static String linesToString(Iterable<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (sb.length() > 0) sb.append("\n");
            sb.append(line);
        }
        return sb.toString();
    }
    
    public static void showMessage(JFrame owner, String title, Iterable<String> lines, int type) {
        showMessage(owner, linesToString(lines), title, type);
    }
    
    public static void showMessage(JFrame owner, String title, String content, int type) {
        JOptionPane.showMessageDialog(owner, content, title, type);
    }
    
    public static void showError(JFrame owner, String title, Iterable<String> lines) {
        showMessage(owner, title, lines, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showError(JFrame owner, String title, String content) {
        showMessage(owner, title, content, JOptionPane.ERROR_MESSAGE);
    }
    
    public static boolean showYesNoDialog(Component owner, String title,
            String content, String yesMessage, String noMessage) {
        return JOptionPane.showOptionDialog(owner, content, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[] {yesMessage, noMessage}, null) == 0;
    }
    
    public static String showInput(Component owner, String title, String content,
            int type) {
        return JOptionPane.showInputDialog(owner, content, title, type);
    }
    
    public static String showQuestion(Component owner, String title, String content) {
        return showInput(owner, content, title, JOptionPane.QUESTION_MESSAGE);
    }
    
    public static<E> E showSelect(Component owner, String title, String content,
            E[] values, E selected) {
        Object ret = JOptionPane.showInputDialog(
                owner, content, title, JOptionPane.QUESTION_MESSAGE, null, values, selected);
        return (ret == null) ? null : (E)ret;
    }
}
