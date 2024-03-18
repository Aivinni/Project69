public class Game {
    private static Space[][] map;

    public Game(int mapX, int mapY, TaskForce[] players) {
        map = new Space[mapY][mapX];
        makeMap();
        for (TaskForce player : players) {
            addToMap(player);
        }
    }

    public Space[][] getMap() {
        return map;
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
        map[position[0]][position[1]] = taskForce;
    }
    public void addToMap(Space space, int[] position){
        map[position[0]][position[1]] = space;
    }
    public Space[][] updateMap(TaskForce[] players) {
        for (Space[] list : map) {
            for (int i = 0; i < list.length; i++) {
                list[i] = new Water("Ocean.png");
            }
        }
        for (TaskForce player : players) {
            addToMap(player);
        }
        return map;
    }
}
