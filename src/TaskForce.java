import javax.swing.*;
import java.awt.*;

public class TaskForce extends Space {
    private String name;
    private int[] position;
    private boolean usingSonar;

    public TaskForce(String name, int[] position, String fileName) {
        super(name, fileName);
        this.name = name;
        this.position = position;
        usingSonar = false;
    }

    public String getName() {
        return name;
    }
    public int[] getPosition() {
        return position;
    }
    public boolean usingSonar(){
        return usingSonar;
    }
    public void updateSonar(boolean a){
        usingSonar = a;
    }

    public void setPosition(int posY, int posX) {
        position[0] = posY;
        position[1] = posX;
    }
}
