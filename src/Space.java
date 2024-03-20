import javax.swing.*;
import java.awt.*;

public class Space {
    private String symbol;
    private Image image;

    public Space(String symbol, String fileName) {
        this.symbol = symbol;
        ImageIcon imageFile = new ImageIcon(fileName);
        image = imageFile.getImage();
    }

    public String getName() {
        return symbol;
    }
    public Image getImage() {
        return image;
    }
}
