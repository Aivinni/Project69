import java.util.ArrayList;

public class Game {
    private static Space[][] map;
    private ArrayList<TaskForce> sprites;
    private ArrayList<Enemy> enemies;
    private ArrayList<Treasure> treasures;

    public Game(int mapX, int mapY) {
        map = new Space[mapY][mapX];
        makeMap();
    }

    public void addSprites(ArrayList<TaskForce> sprites) {
        this.sprites = sprites;
    }
    public void addEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }
    public void addTreasures(ArrayList<Treasure> treasures) {
        this.treasures = treasures;
    }

    public ArrayList<TaskForce> getSprites() {
        return sprites;
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

        if (position[0] < 0) {
            sprite.setPosition(position[0] + 1, position[1]);
        } else if (position[0] >= map.length) {
            sprite.setPosition(position[0] - 1, position[1]);
        } else if (position[1] < 0) {
            sprite.setPosition(position[0], position[1] + 1);
        } else if (position[1] >= map[0].length) {
            sprite.setPosition(position[0], position[1] - 1);
        }
        map[position[0]][position[1]] = sprite;
    }

    public Space[][] updateMap() {
        makeMap();

        for (Treasure treasure : treasures) {
            addToMap(treasure);
        }
        for (Enemy enemy : enemies) {
            addToMap(enemy);
        }
        for (TaskForce sprite : sprites) {
            addToMap(sprite);
        }

        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < sprites.size(); j++) {
                Enemy enemy = enemies.get(i);
                TaskForce player = sprites.get(j);
                if (enemy.getPosition()[0] == player.getPosition()[0] && enemy.getPosition()[1] == player.getPosition()[1]) {
                    if (enemy.isDetected()) {
                        enemy.setSunk(true);
                        enemies.remove(i);
                    } else {
                        player.setSunk(true);
                        sprites.remove(j);
                    }
                    break;
                }
            }
        }

        for (TaskForce sprite : sprites) {
            sprite.setEnemyNear(false);
            for (Enemy enemy : enemies) {
                double distance = getDistance(sprite.getPosition(), enemy.getPosition());
                if (distance <= Math.sqrt(8) && !enemy.getName().equalsIgnoreCase("ace")) {
                    sprite.setEnemyNear(true);
                }
                if (distance > Math.sqrt(2) && enemy.getMovesWhileDetected() > 10) {
                    enemy.setDetected(false);
                    enemy.setMovesWhileDetected(0);
                }
            }
        }

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
                int[] position = new int[]{i, j};
                if (map[i][j] instanceof Treasure) {
                    double distance = getDistance(position, spritePosition);
                    double detectChance = Math.sqrt(distance) / 1.2;
                    if (Math.random() > detectChance) {
                        ((Detectable) map[i][j]).setDetected(true);
                    }
                } if (map[i][j] instanceof Enemy) {
                    double distance = getDistance(position, spritePosition);
                    double detectChance = Math.sqrt(distance) / 1.5;
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
        int endY = Math.min(map.length, spritePosition[0] + 3);
        int endX = Math.min(map[0].length, spritePosition[1] + 3);

        System.out.println(endY - startY);
        System.out.println(endX - startX);

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                int[] position = new int[]{i, j};
                if (map[i][j] instanceof Detectable) {
                    double distance = getDistance(position, spritePosition);
                    double detectChance = distance / 6;
                    if (map[i][j].getName().equalsIgnoreCase("ace")) {
                        detectChance *= 2;
                    }
                    System.out.println(detectChance);
                    if (Math.random() > detectChance) {
                        ((Detectable) map[i][j]).setDetected(true);
                    }
                }
            }
        }
    }

    public TaskForce getClosest(Enemy enemy) {
        TaskForce closest = sprites.get(0);
        double distanceClosest = 0;
        for (TaskForce sprite : sprites) {
            int[] positionSprite = sprite.getPosition();
            int[] positionEnemy = enemy.getPosition();
            double distance = getDistance(positionEnemy, positionSprite);
            if (distance < distanceClosest) {
                distanceClosest = distance;
                closest = sprite;
            }
        }
        return closest;
    }

    public static double getDistance(int[] pos1, int[] pos2) {
        return Math.sqrt(Math.pow(pos1[0] - pos2[0], 2) + Math.pow(pos1[1] - pos2[1], 2));
    }
}
