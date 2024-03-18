import javax.swing.*;
import java.awt.*;

public class TaskForce extends Space {
    private String name;
    private int[] position;

    public TaskForce(String name, int[] position, String fileName) {
        super(name, fileName);
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }
    public int[] getPosition() {
        return position;
    }

    public void setPosition(int posY, int posX) {
        position[0] = posY;
        position[1] = posX;
    }
}
