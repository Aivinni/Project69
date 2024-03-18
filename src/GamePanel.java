import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class GamePanel extends JPanel implements Runnable {
    public static int tile_size = 48; // default tile size is 12
    public static final int MAX_SCREEN_COL = 31; // used for how to draw tiles
    public static final int MAX_SCREEN_ROW = 18;

    private Thread gameThread;
    private KeyHandler keyH;

    private Game game;
    private Space[][] map;
    private TaskForce[] sprites;


    public GamePanel() {
        this.setPreferredSize(new Dimension(tile_size * MAX_SCREEN_COL, tile_size * MAX_SCREEN_ROW));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        TaskForce char1 = new TaskForce("wasd", new int[]{0, 0}, "HMS_Hardy_badge.png");
        TaskForce char2 = new TaskForce("arrows", new int[]{2, 0}, "HMS_Jervis_badge.png");
        Enemy enemy = new Enemy("enemy", new int[]{5, 7}, "Ocean.png");
        sprites = new TaskForce[]{char1, char2, enemy};

        game = new Game(MAX_SCREEN_COL, MAX_SCREEN_ROW, sprites);
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
        int FPS = 60;
        double drawInterval = 1000000000.0 / FPS;

        while (gameThread != null) {
            // system.nanotime is java's very accurate clock or something (i dont 100% remember)
            currentTime = System.nanoTime();

            // the time between now and the last time this looped
            delta += (double) (currentTime - previousTime) / drawInterval;

            previousTime = currentTime;
                // delta being 1 or greater means 1/60 of a second;
            if (delta >= 5) {
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
                if (keyH.isFKeyPressed()){
                    System.out.println("SONAR USED\n");
                    sprites[0].updateSonar(true);
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
        }
    }
    private void move(String direction, int sprite){
        TaskForce a = sprites[sprite];
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
        if (sprites[0].usingSonar()) {
            g2D.setPaint(Color.GREEN);
            int x = sprites[0].getPosition()[1] * tile_size;
            int y = sprites[0].getPosition()[0] * tile_size;
            for (int i = 1; i<6; i++) {
                g2D.drawOval(x, y, tile_size*i, tile_size*i);
                //todo: make the fucking sonar work when you press f
            }
            sprites[0].updateSonar(false);
        }
    }



    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

}