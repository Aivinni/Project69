public class Game {
    private Space[][] map;
    private GamePanel panel;

    public Game() {
        panel = new GamePanel();
        map = new Space[panel.getSizeMap()[0]][panel.getSizeMap()[1]];
    }

    public void makeMap() {

    }
}
