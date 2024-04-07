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

    public GamePanel() {
        this.setPreferredSize(new Dimension(tile_size * MAX_SCREEN_COL, tile_size * MAX_SCREEN_ROW));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        game = new Game(MAX_SCREEN_COL, MAX_SCREEN_ROW);
        map = game.getMap();

        TaskForce char1 = new TaskForce("Player1", new int[]{0, 0}, "HMS_Hardy_badge.png", game);
        TaskForce char2 = new TaskForce("Player2", new int[]{0, 0}, "HMS_Jervis_badge.png", game);
        sprites = new ArrayList<>(Arrays.asList(char1, char2));

        game.addSprites(sprites);

        enemies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int enemyX = (int) (Math.random() * (MAX_SCREEN_COL - 5)) + 5;
            int enemyY = (int) (Math.random() * (MAX_SCREEN_ROW - 5)) + 5;
            Enemy enemy = new Enemy("enemy", new int[]{enemyY, enemyX}, game);
            enemies.add(enemy);
        }

        game.addEnemies(enemies);

        int treasureX = (int) (Math.random() * (MAX_SCREEN_COL - 5)) + 5;
        int treasureY = (int) (Math.random() * (MAX_SCREEN_ROW - 5)) + 5;
        Treasure treasure = new Treasure(new int[]{treasureY, treasureX}, game);
        treasure.setDetected(true);
        System.out.println(Arrays.toString(treasure.getPosition()));
        treasures = new ArrayList<>(Arrays.asList(treasure));

        game.addTreasures(treasures);

        keyH = new KeyHandler();
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startGameThread();
        setUpWindow();
    }

    private void setUpWindow() {
        JFrame window = new JFrame("2D GAME");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();
        window.setVisible(true);
    }
    @Override
    public void run() {
        // variables needed to ensure its locked at 60 fps and below
        long currentTime = System.nanoTime();
        long previousTime = currentTime;
        double delta = 0.0;
        int FPS = 60;
        double drawInterval = 1000000000.0 / FPS;
        double moveTime1 = 0.0;
        double moveTime2 = 0.0;
        double enemyMove = 0.0;

        while (gameThread != null) {
            // system.nanotime is java's very accurate clock or something (i dont 100% remember)
            currentTime = System.nanoTime();

            // the time between now and the last time this looped
            delta += (double) (currentTime - previousTime) / drawInterval;

            if (delta >= 1) {
                repaint();
                if (keyH.isWKeyPressed()) {
                    move("Up", 0);
                }
                if (keyH.isSKeyPressed()) {
                    move("Down", 0);
                }
                if (keyH.isDKeyPressed()) {
                    move("Right", 0);
                }
                if (keyH.isAKeyPressed()) {
                    move("Left", 0);
                }

                if (keyH.isUpKeyPressed()){
                    move("Up", 1);
                }
                if (keyH.isDownKeyPressed()){
                    move("Down", 1);
                }
                if (keyH.isRightKeyPressed()){
                    move("Right", 1);
                }
                if (keyH.isLeftKeyPressed()){
                    move("Left", 1);
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

            moveTime1 += (double) (currentTime - previousTime) / drawInterval;
            moveTime2 += (double) (currentTime - previousTime) / drawInterval;
            enemyMove += (double) (currentTime - previousTime) / drawInterval;

            if (moveTime1 >= 20) {
                sprites.get(0).setMoveReady(true);
                moveTime1 = 0;
            }

            if (moveTime2 >= 20) {
                sprites.get(1).setMoveReady(true);
                moveTime2 = 0;
            }

            if (enemyMove >= 35) {
                for (Enemy enemy : enemies) {
                    enemy.move();
                    enemy.setDetected(true);
                }
                enemyMove = 0;
            }


            if (!sprites.get(0).isActiveSonarJustUsed()) {
                if (keyH.isFKeyPressed()) {
                    sprites.get(0).toggleSonarOn();
                }
            }

            if (!sprites.get(1).isActiveSonarJustUsed()) {
                if (keyH.isSlashKeyPressed()) {
                    sprites.get(1).toggleSonarOn();
                }
            }

            previousTime = currentTime;
        }
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
    public void useSonar(Graphics g, TaskForce sprite){
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
                        g.drawString(String.valueOf(3-(int)((System.nanoTime() - sprite.getLastSonarUseTime()) / 1000000000)), x + (tile_size/4) + (tile_size/5), y - (tile_size/4));
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

        useSonar(g, sprites.get(0));
        useSonar(g, sprites.get(1));


//        boolean showtext = true;
//
//        if (showtext) {
//            g2D.setPaint(Color.black);
//
//            Stroke stroke = new BasicStroke(2.0f);
//            g2D.setStroke(stroke);
//
//
//            int rectWidth = (MAX_SCREEN_COL / 4) * tile_size;
//            int rectCenterX = (MAX_SCREEN_COL * tile_size) / 2;
//            int rectX = rectCenterX - (rectWidth / 2);
//
//            int rectHeight = (MAX_SCREEN_ROW / 6) * tile_size;
//            int rectCenterY = (MAX_SCREEN_ROW * tile_size) / 6;
//            int rectY = rectCenterY - (rectHeight / 2);
//            // Draw border
//            g2D.drawRect(rectX - 1, rectY - 1, rectWidth + 2, rectHeight + 2);
//
//            Color brownishBlack = new Color(35, 26, 26, 200);
//            g2D.setPaint(brownishBlack);
//
//            // Transparent box
//            g2D.fillRect(rectX, rectY, rectWidth, rectHeight);
//
//            g2D.setPaint(Color.white);
//            g2D.drawString("Hello World", rectX, rectCenterY);
//        }
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
}