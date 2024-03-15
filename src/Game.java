public class Game {
    private Space[][] map;

    public Game() {
        map = new Space[GamePanel.getSizeMap()[0]][GamePanel.getSizeMap()[1]];
    }

    public void makeMap() {
        for (Space[] list : map){
            for (int i = 0; i<list.length; i++){
                list[i] = new Water();
            }
        }
    }
}
