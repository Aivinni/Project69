public class Game {
    private Space[][] map;
    private GamePanel panel;

    public Game() {
        panel = new GamePanel(this);
        map = new Space[panel.getSizeMap()[0]][panel.getSizeMap()[1]];
        makeMap();
    }

    public void makeMap() {
        for (Space[] list : map) {
            for (int i = 0; i < list.length; i++) {
                list[i] = new Water();
            }
        }
    }
    public void addToMap(Space space, int x, int y) {
        map[x][y] = space;
    }
    public Space[][] getMap() {
        return map;
    }
}
