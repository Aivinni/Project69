import javax.swing.*;
import java.awt.*;

public class TaskForce extends Space {
    private String name;
    private int[] position;
    private boolean sonarReady;
    private boolean usingSonar;
    private double sonarScale;

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
    public void setSonarReady(boolean sonarReady) {
        this.sonarReady = sonarReady;
    }
    public boolean isSonarReady() {
        return sonarReady;
    }
    public boolean isUsingSonar(){
        return usingSonar;
    }
    public void toggleSonar(){
        usingSonar = !usingSonar;
    }
    public double getSonarScale() {
        return sonarScale;
    }
    public void incrementSonarScale() {
        sonarScale += (0.3 - Math.sqrt(sonarScale / 100));
        if (sonarScale > 3.0) {
            usingSonar = false;
            sonarScale = 0;
        }
    }

    public void setPosition(int posY, int posX) {
        position[0] = posY;
        position[1] = posX;
    }
}
