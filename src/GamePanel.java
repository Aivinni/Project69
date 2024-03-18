import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

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
        sprites = new TaskForce[]{char1, char2};

        keyH = new KeyHandler();
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startGameThread();
        setUpWindow();

        game = new Game(MAX_SCREEN_COL, MAX_SCREEN_ROW, sprites);
        map = game.getMap();
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
                    System.out.println("Up");
                }
                if (keyH.isSKeyPressed()) {
                    move("Down", 0);
                    System.out.println("Down");
                }
                if (keyH.isDKeyPressed()) {
                    move("Right", 0);
                    System.out.println("Right");
                }
                if (keyH.isAKeyPressed()) {
                    move("Left", 0);
                    System.out.println("Left");
                }

                if (keyH.isUpKeyPressed()){
                    move("Up", 1);
                    System.out.println("Up");
                }
                if (keyH.isDownKeyPressed()){
                    move("Down", 1);
                    System.out.println("Down");
                }
                if (keyH.isRightKeyPressed()){
                    move("Right", 1);
                    System.out.println("Right");
                }
                if (keyH.isLeftKeyPressed()){
                    move("Left", 1);
                    System.out.println("Left");
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

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                g.drawImage(map[i][j].getImage(), j * tile_size, i * tile_size, tile_size, tile_size, null);
            }
        }
//        System.out.println("test");

//        g2D.setColor(Color.BLACK);
//        g2D.fillRect(sprites[0].getPosition()[1] * tile_size, sprites[0].getPosition()[0] * tile_size, tile_size, tile_size);
//        ImageIcon bg = new ImageIcon("HMS_Hardy_badge.png");
//        Image bgImage = bg.getImage();
//        g.drawImage(bgImage, sprites[0].getPosition()[1] * tile_size, sprites[0].getPosition()[0] * tile_size, tile_size, tile_size, null);
//
//
//        g.setColor(Color.PINK);
//        g.fillRect(sprites[1].getPosition()[1] * tile_size, sprites[1].getPosition()[0] * tile_size, tile_size, tile_size);
//        bg = new ImageIcon("HMS_Jervis_badge.png");
//        bgImage = bg.getImage();
//        g.drawImage(bgImage, sprites[1].getPosition()[1] * tile_size, sprites[1].getPosition()[0] * tile_size, tile_size, tile_size, null);


    }


    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

}