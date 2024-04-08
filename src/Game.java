import java.util.ArrayList;

public class Game {
    private static Space[][] map;
    private ArrayList<TaskForce> sprites = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Treasure> treasures = new ArrayList<>();
    private ArrayList<Interactive> list = new ArrayList<>();
    private GamePanel gamePanel;

    public Game(int mapX, int mapY, GamePanel gamePanel) {
        map = new Space[mapY][mapX];
        makeMap();
        this.gamePanel = gamePanel;
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
    public void makeList(){
        for (Treasure treasure : treasures) {
            list.add(treasure);
        }
        for (Enemy enemy : enemies) {
            list.add(enemy);
        }
        for (TaskForce sprite : sprites) {
            list.add(sprite);
        }
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
                        enemies.remove(i);
                        Message message = new Message(enemy.getName() + " sunk! \n Well Done!", true, 0);
                        gamePanel.setMessage(message);
                    } else {
                        sprites.remove(j);
                        Message message = new Message(player.getName() + " sunk!", true, 0);
                        gamePanel.setMessage(message);
                    }
                    break;
                }
            }
        }

        for (TaskForce sprite : sprites) {
            sprite.setEnemyNear(false);
            for (Enemy enemy : enemies) {
                double distance = getDistance(sprite.getPosition(), enemy.getPosition());
                if (distance <= Math.sqrt(8) && !enemy.getName().equalsIgnoreCase("U-boat ace")) {
                    sprite.setEnemyNear(true);
                }
                if (distance > Math.sqrt(2) && enemy.getMovesWhileDetected() > 10) {
                    enemy.setDetected(false);
                    enemy.setMovesWhileDetected(0);
                }
            }

            for (int i = 0; i < treasures.size(); i++) {
                Treasure treasure = treasures.get(i);
                if (treasure.isDetected() && treasure.getPosition()[0] == sprite.getPosition()[0] && treasure.getPosition()[1] == sprite.getPosition()[1]) {
                    treasures.remove(i);
                    i--;
                    gamePanel.setTreasureFound(true);
                    gamePanel.setTreasureOn(sprite);
                    Message message = new Message("Gold retrieved! \nWarning: U-boats in Area have been alerted to your position \nReturn to (0, 0) to win the game!", true, 0);
                    gamePanel.setMessage(message);
                    for (Enemy enemy : enemies) {
                        enemy.setPositionTarget(sprite.getPosition());
                    }
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
                    double chance = Math.random();
                    if (chance > detectChance) {
                        ((Detectable) map[i][j]).setDetected(true);
                        Message message = new Message("Wreck of HMS Edinburgh Located! \nWarning: U-boats in Area have been alerted to your position", true, 0);
                        gamePanel.setMessage(message);
                        for (Enemy enemy : enemies) {
                            enemy.setPositionTarget(spritePosition);
                        }
                    }
                }
                if (map[i][j] instanceof Enemy) {
                    double distance = getDistance(position, spritePosition);
                    double detectChance = Math.sqrt(distance) / 1.5;
                    double chance = Math.random();
                    if (chance > detectChance) {
                        ((Detectable) map[i][j]).setDetected(true);
                        Message message = new Message(map[i][j].getName() + " detected! \nMoving over the enemy while they are detected \nwill result in their sinking", true, 0);
                        gamePanel.setMessage(message);
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

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                int[] position = new int[]{i, j};
                if (map[i][j] instanceof Detectable) {
                    double distance = getDistance(position, spritePosition);
                    double detectChance = distance / 6;
                    if (map[i][j].getName().equalsIgnoreCase("ace")) {
                        detectChance *= 2;
                    }
                    double chance = Math.random();
                    if (chance > detectChance) {
                        ((Detectable) map[i][j]).setDetected(true);
                        if (map[i][j] instanceof Treasure) {
                            Message message = new Message("Wreck of HMS Edinburgh Located! \n Warning: U-boats in Area have been alerted to your position", true, 0);
                            gamePanel.setMessage(message);
                        }
                        if (map[i][j] instanceof Enemy) {
                            Message message = new Message(map[i][j].getName() + " detected! \nMoving over the enemy while they are detected \nwill result in their sinking", true, 0);
                            gamePanel.setMessage(message);
                        }
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
