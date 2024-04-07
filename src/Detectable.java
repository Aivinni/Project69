public class Detectable extends Interactive {
    private String normalImage;
    private String hiddenImage;
    private boolean detected;
    public Detectable(String name, int[] position, String fileName, String hiddenImage, Game game) {
        super(name, position, fileName, game);
        normalImage = fileName;
        this.hiddenImage = hiddenImage;
        detected = false;
    }
    public void setDetected(boolean detected) {
        this.detected = detected;
        if (detected) {
            super.updateImage(hiddenImage);
        } else {
            super.updateImage(normalImage);
        }
    }
    public boolean isDetected() {
        return detected;
    }
}
