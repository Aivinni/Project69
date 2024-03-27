import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class GamePanel extends JPanel implements Runnable {
    public static int tile_size = 48; // default tile size is 12
    public static final int MAX_SCREEN_COL = 31; //previously 31
    public static final int MAX_SCREEN_ROW = 18; //previously 18

    private Thread gameThread;
    private KeyHandler keyH;

    private Game game;
    private Space[][] map;
    private TaskForce[] sprites;
    private int x;
    private int y;
    private int posX;
    private int posY;

    private long lastSonarUseTime;
    private long lastPassivePulseTime;


    public GamePanel() {
        this.setPreferredSize(new Dimension(tile_size * MAX_SCREEN_COL, tile_size * MAX_SCREEN_ROW));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        TaskForce char1 = new TaskForce("wasd", new int[]{0, 0}, "HMS_Hardy_badge.png");
        TaskForce char2 = new TaskForce("arrows", new int[]{2, 0}, "HMS_Jervis_badge.png");
        Treasure treasure = new Treasure();
        Enemy enemy = new Enemy("enemy", new int[]{5, 7});
        sprites = new TaskForce[]{char1, char2};

        game = new Game(MAX_SCREEN_COL, MAX_SCREEN_ROW, sprites);
        game.addToMap(treasure);
        game.addToMap(enemy);
        map = game.getMap();

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
        double sonarTime = 0.0;
        int FPS = 60;
        double drawInterval = 1000000000.0 / FPS;

        while (gameThread != null) {
            // system.nanotime is java's very accurate clock or something (i dont 100% remember)
            currentTime = System.nanoTime();

            // the time between now and the last time this looped
            delta += (double) (currentTime - previousTime) / drawInterval;

            if (delta % (int) delta >= 0.999995) {
                repaint();
            }

            if (delta >= 6) {
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
            }

            sonarTime += (double) (currentTime - previousTime) / drawInterval;

            if (!sprites[0].isActiveSonarJustUsed()) {
                if (keyH.isFKeyPressed()) {
                    sprites[0].toggleSonarOn();
                    sprites[0].setSonarReady(false);
                }
            }
            x = sprites[0].getPosition()[1] * tile_size;
            y = sprites[0].getPosition()[0] * tile_size;
            posX = sprites[1].getPosition()[1]*tile_size;
            posY = sprites[1].getPosition()[0]*tile_size;

            previousTime = currentTime;
        }
    }
    private void move(String direction, int sprite){
        Interactive a = sprites[sprite];
        if (direction.equalsIgnoreCase("Up")&& a.getPosition()[0] > 0) {
            a.setPosition(a.getPosition()[0] - 1, a.getPosition()[1]);
        }
        if (direction.equalsIgnoreCase("Left")&& a.getPosition()[1] > 0) {
            a.setPosition(a.getPosition()[0], a.getPosition()[1] - 1);
        }
        if (direction.equalsIgnoreCase("Down")&& a.getPosition()[0] < MAX_SCREEN_ROW - 1) {
            a.setPosition(a.getPosition()[0] + 1, a.getPosition()[1]);
        }
        if (direction.equalsIgnoreCase("Right")&& a.getPosition()[1] < MAX_SCREEN_COL - 1) {
            a.setPosition(a.getPosition()[0], a.getPosition()[1] + 1);
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

       Graphics2D g2D = (Graphics2D) g;

        map = game.updateMap(sprites);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                g.drawImage(map[i][j].getImage(), j * tile_size, i * tile_size, tile_size, tile_size, null);
            }
        }


        double active;
        double passive;
        if (sprites[0].isUsingSonar()) {
            active = sprites[0].getSonarScale();
            float alpha = 1 - ((float) active / 7);
            Color color = new Color(0, 1, 0, alpha);
            g2D.setPaint(color);
            g2D.drawOval((int) (x - ((tile_size * active) / 2)) + (tile_size / 2), (int) (y - ((tile_size * active) / 2)) + (tile_size / 2), (int) (tile_size * active), (int) (tile_size * active));
            sprites[0].incrementSonarScale();
            if (sprites[0].isActiveSonarJustUsed()) {
                lastSonarUseTime = System.nanoTime();
            }
            sprites[0].resetPassiveSonarScale();
        } else {
            if (!sprites[0].isActiveSonarJustUsed()) {
                if (!sprites[0].isPassiveSonarJustUsed()) {
                    passive = sprites[0].getPassiveSonarScale();
                    float alpha = 1 - ((float) passive / 4);
                    Color color = new Color(0, 1, 0, alpha);
                    g2D.setPaint(color);
                    g2D.drawOval((int) (x - ((tile_size * passive) / 2)) + (tile_size / 2), (int) (y - ((tile_size * passive) / 2)) + (tile_size / 2), (int) (tile_size * passive), (int) (tile_size * passive));
                    sprites[0].incrementPassiveSonarScale();
                    if (sprites[0].isPassiveSonarJustUsed()) {
                        lastPassivePulseTime = System.nanoTime();
                    }
                } else {
                    long passiveDelay = 500000000;
                    if (System.nanoTime() - lastPassivePulseTime > passiveDelay) {
                        sprites[0].setPassiveSonarJustUsed(false);
                    }
                }

            } else {
                long delay = 3;
                if ((System.nanoTime() - lastSonarUseTime) / 1000000000 > delay) {
                    sprites[0].setActiveSonarJustUsed(false);
                } else {
                    g.setColor(Color.CYAN);
                    g.setFont(new Font("SansSerif", Font.PLAIN, 18 ));
                    if (sprites[0].getPosition()[0] > (MAX_SCREEN_ROW/2)-3) {
                        //for cooldown above
                        g.drawString(String.valueOf(3-(int)((System.nanoTime() - lastSonarUseTime) / 1000000000)), x + (tile_size/4) + (tile_size/5), y - (tile_size/4));
                    } else {
                        //for cooldown below
                        g.drawString(String.valueOf(3 - (int) ((System.nanoTime() - lastSonarUseTime) / 1000000000)), x + (tile_size / 4) + (tile_size / 5), y + tile_size + (tile_size / 2));
                    }
                }
            }
        }
        passive = sprites[1].getPassiveSonarScale();
        float alpha = 1 - ((float) passive / 4);
        Color color = new Color(0, 1, 0, alpha);
        g2D.setPaint(color);
        g2D.drawOval((int) (posX - ((tile_size * passive) / 2)) + (tile_size / 2), (int) (posY - ((tile_size * passive) / 2)) + (tile_size / 2), (int) (tile_size * passive), (int) (tile_size * passive));
        sprites[1].incrementPassiveSonarScale();


        Color black = new Color(0, 0, 0);
        g2D.setPaint(black);

        Stroke stroke = new BasicStroke(2.0f);
        g2D.setStroke(stroke);

        g2D.drawRect(((MAX_SCREEN_COL * tile_size) / 2) - (((MAX_SCREEN_COL / 4) * tile_size) / 2) - 1, tile_size - 1, (MAX_SCREEN_COL / 4) * tile_size + 2, (MAX_SCREEN_ROW / 6) * tile_size + 2);

        Color brownishBlack = new Color(35, 26, 26, 200);
        g2D.setPaint(brownishBlack);

        g2D.fillRect(((MAX_SCREEN_COL * tile_size) / 2) - (((MAX_SCREEN_COL / 4) * tile_size) / 2), tile_size, (MAX_SCREEN_COL / 4) * tile_size, (MAX_SCREEN_ROW / 6) * tile_size);
    }



    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


}