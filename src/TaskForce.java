import javax.swing.*;
import java.awt.*;

public class TaskForce extends Space {
    private String keyLink;
    private int[] position;

    public TaskForce(String keyLink, int[] position, String fileName) {
        super("player", fileName);
        this.keyLink = keyLink;
        this.position = position;
    }

    public String getKeyLink() {
        return keyLink;
    }
    public int[] getPosition() {
        return position;
    }

    public void setPosition(int posY, int posX) {
        position[0] = posY;
        position[1] = posX;
    }
}
