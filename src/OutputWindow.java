import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class OutputWindow {
    // OutputWindow variables
    public JFrame frame;
    private JTextPane textPane;
    private StyledDocument doc;
    private Style style;


    // Makes a window
    public OutputWindow() {
        frame = new JFrame("Cee-lo Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1470, 956);
        frame.setLocation(500, 500);
        textPane = new JTextPane();
        textPane.setEditable(false);
        doc = textPane.getStyledDocument();
        style = doc.addStyle("my style", null);
        StyleConstants.setFontSize(style, 36);
        frame.add(textPane);
        frame.setVisible(true);
    }
    // Adds text
    public void addTextToWindow(String text, Color color) {
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style); }
        catch (Exception ignored) { }
    }
    public void clear() {
        frame.getContentPane().removeAll();
        textPane.setText("");
        frame.add(textPane);
    }
}