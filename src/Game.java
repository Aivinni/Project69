public class Game {
    private static Space[][] map;
    private GamePanel panel;

    public Game() {
        panel = new GamePanel();
        map = new Space[panel.getSizeMap()[0]][panel.getSizeMap()[1]];
        makeMap();
        addToMap(panel.getPlayers(), 0);
        for (Space[] list: map){
            for (Space space : list){
                System.out.print(space.getSymbol() + " ");
            }
            System.out.println();
        }
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
    public void addToMap(Space[] list, int x){
        for (int i = 0; i<list.length; i++){
            map[x][i] = list[i];
        }
    }
    public static Space[][] getMap() {
        return map;
    }
}
