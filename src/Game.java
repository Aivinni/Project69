public class Game {
    private Space[][] map;
    private GamePanel panel;

    public Game() {
        panel = new GamePanel();
        map = new Space[panel.getSizeMap()[0]][panel.getSizeMap()[1]];
    }

    public void makeMap() {
        for (Space[] list : map){
            for (int i = 0; i<list.length; i++){
                list[i] = new Water();
            }
        }
    }
}
