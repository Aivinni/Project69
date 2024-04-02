import java.awt.*;

public class Detectable extends Interactive {
    private String hiddenImage;
    private boolean detected;
    public Detectable(String name, int[] position, String fileName, String hiddenImage) {
        super(name, position, fileName);
        this.hiddenImage = hiddenImage;
        detected = false;
    }
    public void setDetected(boolean detected) {
        this.detected = detected;
        if (detected) {
            super.updateImage(hiddenImage);
        }
    }
    public boolean isDetected() {
        return detected;
    }

}
