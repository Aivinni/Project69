public class Game {
    private static Space[][] map;

    public Game(int mapX, int mapY, Interactive[] players) {
        map = new Space[mapY][mapX];
        makeMap();
        addToMap(players);
    }

    public Space[][] getMap() {
        return map;
    }

    public void makeMap() {
        for (Space[] list : map) {
            for (int i = 0; i < list.length; i++) {
                list[i] = new Water();
            }
        }
    }
    public void addToMap(Interactive sprite) {
        int[] position = sprite.getPosition();
        map[position[0]][position[1]] = sprite;
    }
    public void addToMap(Interactive[] spaces){
        for (Interactive space: spaces){
            addToMap(space);
        }
    }
    public Space[][] updateMap(Interactive[] players, Enemy[] enemies, Treasure[] treasures) {
        makeMap();
        addToMap(enemies);
        addToMap(treasures);
        addToMap(players);
        return map;
    }

    public void detectWithPassive(TaskForce sprite) {
        System.out.println("detect with passive");
        int[] spritePosition = sprite.getPosition();

        int startY = Math.max(0, spritePosition[0] - 1);
        int startX = Math.max(0, spritePosition[1] - 1);
        int endY = Math.min(map.length, spritePosition[0] + 1);
        int endX = Math.min(map[0].length, spritePosition[1] + 1);

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                if (map[i][j] instanceof Detectable) {
                    double distance = Math.sqrt(Math.pow(i - spritePosition[0], 2) + Math.pow(j - spritePosition[1], 2));
                    double detectChance = Math.sqrt(distance) / 1.3;
                    System.out.println(detectChance);
                    if (Math.random() > detectChance) {
                        ((Detectable) map[i][j]).setDetected(true);
                    }
                }
            }
        }
    }

    public void detectWithActive(TaskForce sprite) {
        System.out.println("detect with active");
        int[] spritePosition = sprite.getPosition();

        int startY = Math.max(0, spritePosition[0] - 2);
        int startX = Math.max(0, spritePosition[1] - 2);
        int endY = Math.min(map.length, spritePosition[0] + 2);
        int endX = Math.min(map[0].length, spritePosition[1] + 2);

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                if (map[i][j] instanceof Detectable) {
                    double distance = Math.sqrt(Math.pow(i - spritePosition[0], 2) + Math.pow(j - spritePosition[1], 2));
                    double detectChance = distance / 6;
                    if (Math.random() > detectChance) {
                        ((Detectable) map[i][j]).setDetected(true);
                    }
                }
            }
        }
    }
}
