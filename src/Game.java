public class Game {
    private static Space[][] map;
    private TaskForce[] players;

    public Game(int mapX, int mapY, TaskForce[] players) {
        map = new Space[mapY][mapX];
        makeMap();
        for (TaskForce player : players) {
            addToMap(player);
        }
    }

    public void makeMap() {
        for (Space[] list : map) {
            for (int i = 0; i < list.length; i++) {
                list[i] = new Water("Ocean.png");
            }
        }
    }
    public void addToMap(TaskForce taskForce) {
        int[] position = taskForce.getPosition();
        map[position[1]][position[0]] = taskForce;
    }
    public Space[][] getMap() {
        return map;
    }
}
