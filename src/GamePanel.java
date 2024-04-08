import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GamePanel extends JPanel implements Runnable {
    public static int tile_size = 48; //don't change this

    //for aivin: 31
    //for normal people: 40
    public static final int MAX_SCREEN_COL = 31;
    //for aivin: 18
    //for normal people: 21
    public static final int MAX_SCREEN_ROW = 18;

    private Thread gameThread;
    private KeyHandler keyH;

    private Game game;
    private Space[][] map;
    private ArrayList<TaskForce> sprites;
    private ArrayList<Enemy> enemies;
    private ArrayList<Treasure> treasures;
    private int x;
    private int y;

    private Message message;

    private boolean treasureFound = false;
    private TaskForce treasureOn;
    private boolean victory = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(tile_size * MAX_SCREEN_COL, tile_size * MAX_SCREEN_ROW));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        game = new Game(MAX_SCREEN_COL, MAX_SCREEN_ROW, this);
        map = game.getMap();

        TaskForce char1 = new TaskForce("HMS Hardy (Player 1)", new int[]{0, 0}, game, "HMS_Hardy_badge.png", "f", "w", "s", "a", "d");
        TaskForce char2 = new TaskForce("HMS Jervis (Player 2)", new int[]{0, 0}, game, "HMS_Jervis_badge.png", "/", "up", "down", "left", "right");
        sprites = new ArrayList<>(Arrays.asList(char1, char2));

        game.addSprites(sprites);

        enemies = new ArrayList<>();

        int enemyX = (int) (Math.random() * (MAX_SCREEN_COL - 5)) + 5;
        int enemyY = (int) (Math.random() * (MAX_SCREEN_ROW - 5)) + 5;
        Enemy enemy = new Enemy("U-boat ace", new int[]{enemyY, enemyX},"154.png", game);
        enemies.add(enemy);
        for (int i = 0; i < 3; i++) {
            enemyX = (int) (Math.random() * (MAX_SCREEN_COL - 5)) + 5;
            enemyY = (int) (Math.random() * (MAX_SCREEN_ROW - 5)) + 5;
            enemy = new Enemy("U-boat", new int[]{enemyY, enemyX},"Uboat.png", game);
            enemies.add(enemy);
        }

        game.addEnemies(enemies);

        int treasureX = (int) (Math.random() * (MAX_SCREEN_COL - 5)) + 5;
        int treasureY = (int) (Math.random() * (MAX_SCREEN_ROW - 5)) + 5;
        Treasure treasure = new Treasure(new int[]{treasureY, treasureX}, game);
        System.out.println(Arrays.toString(treasure.getPosition()));
        treasures = new ArrayList<>(Arrays.asList(treasure));

        game.addTreasures(treasures);
        game.makeList();

        message = new Message("Mission: Locate The Wreck of The Edinburgh and return to coordinates (0,0) \n" +
                                "Warning: Multiple U-boats detected in area: Proceed with caution \n" +
                                "Being on the same tile as a U-boat while they are undetected will result in your sinking", true, 0);

        keyH = new KeyHandler(game);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startGameThread();
        setUpWindow();
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    public void setTreasureFound(boolean treasureFound) {
        this.treasureFound = treasureFound;
    }
    public void setTreasureOn(TaskForce treasureOn) {
        this.treasureOn = treasureOn;
    }

    private void setUpWindow() {
        JFrame window = new JFrame("2D GAME");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();
        window.setVisible(true);
    }
    private void move(String direction, int spriteIdx){
        TaskForce sprite = sprites.get(spriteIdx);
        if (direction.equalsIgnoreCase("Up") && sprite.getPosition()[0] > 0 && sprite.isMoveReady()) {
            sprite.setPosition(sprite.getPosition()[0] - 1, sprite.getPosition()[1]);
            sprite.setMove("up");
            sprite.setMoveReady(false);
        }
        if (direction.equalsIgnoreCase("Left") && sprite.getPosition()[1] > 0 && sprite.isMoveReady()) {
            sprite.setPosition(sprite.getPosition()[0], sprite.getPosition()[1] - 1);
            sprite.setMoveReady(false);
            sprite.setMove("left");
        }
        if (direction.equalsIgnoreCase("Down") && sprite.getPosition()[0] < MAX_SCREEN_ROW - 1 && sprite.isMoveReady()) {
            sprite.setPosition(sprite.getPosition()[0] + 1, sprite.getPosition()[1]);
            sprite.setMoveReady(false);
            sprite.setMove("down");
        }
        if (direction.equalsIgnoreCase("Right") && sprite.getPosition()[1] < MAX_SCREEN_COL - 1 && sprite.isMoveReady()) {
            sprite.setPosition(sprite.getPosition()[0], sprite.getPosition()[1] + 1);
            sprite.setMoveReady(false);
            sprite.setMove("right");
        }
        sprite.setMoveDelay(24);
    }
    @Override
    public void run() {
        // variables needed to ensure its locked at 60 fps and below
        long currentTime = System.nanoTime();
        long previousTime = currentTime;
        double delta = 0.0;
        int FPS = 60;
        double drawInterval = 1000000000.0 / FPS;
        double enemyMove = 0.0;

        while (gameThread != null) {
            // system.nanotime is java's very accurate clock or something (i dont 100% remember)
            currentTime = System.nanoTime();

            double timePassed = (double) (currentTime - previousTime) / drawInterval;
            // the time between now and the last time this looped
            delta += timePassed;

            if (delta >= 1) {
                repaint();

                for (int i = 0; i < sprites.size(); i++) {
                    String[] key = keyH.getMovementKey();
                    TaskForce sprite = sprites.get(i);
                    for (String s : key) {
                        if (s.equalsIgnoreCase(sprite.getUpKey())) {
                            move("Up", i);
                        } else if (s.equalsIgnoreCase(sprite.getDownKey())) {
                            move("Down", i);
                        } else if (s.equalsIgnoreCase(sprite.getLeftKey())) {
                            move("Left", i);
                        } else if (s.equalsIgnoreCase(sprite.getRightKey())) {
                            move("Right", i);
                        }
                    }
                }
                delta = 0;

                for (TaskForce sprite : sprites) {
                    if (sprite.getMoveDelay() >= 6) {
                        sprite.setMoveDelay(sprite.getMoveDelay() - 1);
                    } else {
                        sprite.setMoveDelay(0);
                        sprite.setMove(null);
                    }
                }
            }
            for (TaskForce sprite : sprites) {
                if (sprite == null) {
                    break;
                }
                sprite.setMoveTime(sprite.getMoveTime() + timePassed);
                if (sprite.getMoveTime() >= 20) {
                    sprite.setMoveReady(true);
                    sprite.setMoveTime(0);
                }
                if (!sprite.isActiveSonarJustUsed()) {
                    if (keyH.getInteractKeyPressed().equals(sprite.getSonarKey())) {
                        sprite.toggleSonarOn();
                    }
                }
            }

            enemyMove += timePassed;

            if (enemyMove >= 35) {
                for (Enemy enemy : enemies) {
                    enemy.move();
                }
                enemyMove = 0;
            }

            if (message.getTimeShown() >= 500) {
                message.setVisible(false);
            } else {
                message.setTimeShown(message.getTimeShown() + timePassed);
            }

            previousTime = currentTime;
        }
    }

    private void useSonar(Graphics g, TaskForce sprite){
        Graphics2D g2D = (Graphics2D) g;

        x = sprite.getPosition()[1] * tile_size;
        y = sprite.getPosition()[0] * tile_size;

        double active;
        double passive;
        if (sprite.isUsingSonar()) {
            // If currently using Sonar, draw oval for sonar
            active = sprite.getSonarScale();
            float alpha = 1 - ((float) active / 7);
            Color color = new Color(0, 1, 0, alpha);
            g2D.setPaint(color);
            g2D.drawOval((int) (x - ((tile_size * active) / 2)) + (tile_size / 2), (int) (y - ((tile_size * active) / 2)) + (tile_size / 2), (int) (tile_size * active), (int) (tile_size * active));
            sprite.incrementSonarScale();
            if (sprite.isActiveSonarJustUsed()) {
                // lastSonarUseTime = System.nanoTime();
                sprite.setLastSonarUseTime();
                game.detectWithActive(sprite);
                sprite.resetPassiveSonarScale();
                for (Enemy enemy : enemies) {
                    if (enemy.isTargetReached()) {
                        int[] tempPos = new int[sprite.getPosition().length];
                        for (int i = 0; i < sprite.getPosition().length; i++) {
                            tempPos[i] = sprite.getPosition()[i];
                        }
                        enemy.setPositionTarget(tempPos);
                    }
                }
            }

        } else {
            if (!sprite.isActiveSonarJustUsed()) {
                if (!sprite.isPassiveSonarJustUsed()) {
                    passive = sprite.getPassiveSonarScale();
                    float alpha = 1 - ((float) passive / 4);
                    Color color = new Color(0, 1, 0, alpha);
                    g2D.setPaint(color);
                    g2D.drawOval((int) (x - ((tile_size * passive) / 2)) + (tile_size / 2), (int) (y - ((tile_size * passive) / 2)) + (tile_size / 2), (int) (tile_size * passive), (int) (tile_size * passive));
                    sprite.incrementPassiveSonarScale();
                    if (sprite.isPassiveSonarJustUsed()) {
                        //lastPassivePulseTime = System.nanoTime();
                        sprite.setLastPassivePulseTime();
                        game.detectWithPassive(sprite);
                    }
                } else {
                    long passiveDelay = 750000000;
                    if (System.nanoTime() - sprite.getLastPassivePulseTime() > passiveDelay) {
                        sprite.setPassiveSonarJustUsed(false);
                    }
                }

            } else {
                long delay = 3;
                if ((System.nanoTime() - sprite.getLastSonarUseTime()) / 1000000000 > delay) {
                    sprite.setActiveSonarJustUsed(false);
                } else {
                    g.setColor(Color.CYAN);
                    g.setFont(new Font("SansSerif", Font.PLAIN, 18 ));
                    if (sprite.getPosition()[0] > (MAX_SCREEN_ROW/2)-3) {
                        //for cooldown above
                        g.drawString(String.valueOf(3-(int)((System.nanoTime() - sprite.getLastSonarUseTime()) / 1000000000)), x + (tile_size / 4) + (tile_size / 5), y - (tile_size / 4));
                    } else {
                        //for cooldown below
                        g.drawString(String.valueOf(3 - (int) ((System.nanoTime() - sprite.getLastSonarUseTime()) / 1000000000)), x + (tile_size / 4) + (tile_size / 5), y + tile_size + (tile_size / 2));
                    }
                }
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (sprites.isEmpty()) {
            g.setColor(Color.red);
            g.setFont(new Font("times", Font.BOLD, 60 ));
            g.drawString("ALL FORCES SUNK: MISSION FAILED", ((MAX_SCREEN_COL * tile_size) / 6), (MAX_SCREEN_ROW * tile_size) / 2);
        } else if (victory) {
            g.setColor(Color.yellow);
            g.setFont(new Font("times", Font.BOLD, 72 ));
            g.drawString("VICTORY", ((MAX_SCREEN_COL * tile_size) / 2) - ((MAX_SCREEN_COL * tile_size) / 6), (MAX_SCREEN_ROW * tile_size) / 2);
        } else {
            map = game.updateMap();
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    Space tile = map[i][j];
                    if (tile instanceof Interactive interactiveTile) {
                        if (!(interactiveTile.getMove() == null)) {
                            switch (interactiveTile.getMove()) {
                                case "up" ->
//                            g.drawImage(tile.getImage(), j * tile_size, i * tile_size + interactiveTile.getMoveDelay(), tile_size, tile_size, null);
                                        g.drawImage(tile.getImage(), j * tile_size, i * tile_size, tile_size, tile_size, null);
                                case "down" ->
                                        g.drawImage(tile.getImage(), j * tile_size, i * tile_size, tile_size, tile_size, null);
                                case "left" ->
                                        g.drawImage(tile.getImage(), j * tile_size, i * tile_size, tile_size, tile_size, null);
                                case "right" ->
                                        g.drawImage(tile.getImage(), j * tile_size, i * tile_size, tile_size, tile_size, null);
                            }
                        } else {
                            g.drawImage(tile.getImage(), j * tile_size, i * tile_size, tile_size, tile_size, null);
                        }

                    } else {
                        g.drawImage(tile.getImage(), j * tile_size, i * tile_size, tile_size, tile_size, null);
                    }
                }
            }

            for (TaskForce sprite : sprites) {
                useSonar(g, sprite);
                if (sprite.isEnemyNear()) {
                    g.setColor(Color.red);
                    g.setFont(new Font("SansSerif", Font.PLAIN, 18 ));
                    if (sprite.getPosition()[0] > (MAX_SCREEN_ROW/2)-3) {
                        //for above
                        g.drawString("Enemy Near", x - (tile_size / 2), y - tile_size);
                    } else {
                        //for below
                        g.drawString("Enemy Near", x - (tile_size / 2), y + (2 * tile_size));
                    }
                }
            }

            if (message.isVisible()) {
                Color black = new Color(0, 0, 0, 100);
                g.setColor(black);

                int rectWidth = (MAX_SCREEN_COL / 3) * tile_size;
                int rectCenterX = (MAX_SCREEN_COL * tile_size) / 2;
                int rectX = rectCenterX - (rectWidth / 2);

                int rectHeight = (MAX_SCREEN_ROW / 8) * tile_size;
                int rectCenterY = (MAX_SCREEN_ROW * tile_size) / 6;
                int rectY = rectCenterY - (rectHeight / 2);
                // Draw border
                g.fillRect(rectX - 1, rectY - 1, rectWidth + 2, rectHeight + 2);

                Color brownishBlack = new Color(35, 26, 26, 200);
                g.setColor(brownishBlack);

                // Transparent box
                g.fillRect(rectX, rectY, rectWidth, rectHeight);

                g.setColor(Color.white);
                g.setFont(new Font("Times", Font.PLAIN, 10));

                // Split text into lines
                String[] lines = message.getMessage().split("\n");

                int x = rectX;
                int y = rectCenterY - (rectHeight / 4);
                // Draw each line
                for (String line : lines) {
                    if (line.contains("Warning")) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.drawString(line, x, y);
                    // Move to the next line
                    y += g.getFontMetrics().getHeight();
                }
            }

            if (treasureFound) {
                for (TaskForce sprite : sprites) {
                    if (sprite == treasureOn && sprite.getPosition()[0] == 0 && sprite.getPosition()[1] == 0) {
                        victory = true;
                    }
                }

                Color gold = new Color(255, 215, 0, 150);
                Graphics2D g2d = (Graphics2D) g;

                g2d.setStroke(new BasicStroke(5));
                g.setColor(gold);
                g2d.drawRect(0, 0, tile_size, tile_size);
            }
        }
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
}