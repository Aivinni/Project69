public class Interactive extends Space{
    private int[] position;

    public Interactive(String name, int[] position, String fileName) {
        super(name, fileName);
        this.position = position;
    }
    public void setPosition(int posY, int posX) {
        position[0] = posY;
        position[1] = posX;
    }
    public int[] getPosition() {
        return position;
    }
}
