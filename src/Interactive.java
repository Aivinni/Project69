public class Interactive extends Space{
    private int[] position;
    private int moveDelay;
    private String move;
    private boolean sunk;
    private Game game;

    public Interactive(String name, int[] position, String fileName, Game game) {
        super(name, fileName);
        this.position = position;
        this.game = game;
    }
    public void setPosition(int posY, int posX) {
        position[0] = posY;
        position[1] = posX;
    }
    public int[] getPosition() {
        return position;
    }

    public void setMoveDelay(int moveDelay) {
        this.moveDelay = moveDelay;
    }
    public int getMoveDelay() {
        return moveDelay;
    }

    public void setMove(String move) {
        this.move = move;
    }
    public String getMove() {
        return move;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }
    public boolean isSunk() {
        return sunk;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void updateImage(String fileName) {
        super.updateImage(fileName);
    }
    //TODO: MAKE THE SEA APPEAR AS THE BACKGROUND INSTEAD OF A BLACK BACKGROUND
}
