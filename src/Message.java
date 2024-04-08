public class Message {
    private String message;
    private boolean visible;
    private double timeShown;

    public Message(String message, boolean visible, double timeShown) {
        this.message = message;
        this.visible = visible;
        this.timeShown = timeShown;
    }

    public String getMessage() {
        return message;
    }
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public double getTimeShown() {
        return timeShown;
    }
    public void setTimeShown(double timeShown) {
        this.timeShown = timeShown;
    }
}
