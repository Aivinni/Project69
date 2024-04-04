public class Interactive extends Space{
    private int[] position;

    public Interactive(String name, int[] position, String fileName) {
        super(name, fileName);
        this.position = position;
    }
    public void setPosition(int posY, int posX) {
        position[0] = posY;
        position[1] = posX;
        //todo: CHANGE THIS SHIT TO BELOW AND DEAL WITH ALL THE SHIT THAT BREAKS
        //position[0] = posX;
        //position[1] = posY;

    }
    public int[] getPosition() {
        return position;
    }

    @Override
    public void updateImage(String fileName) {
        super.updateImage(fileName);
    }
    //TODO: MAKE THE SEA APPEAR AS THE BACKGROUND INSTEAD OF A BLACK BACKGROUND
}
