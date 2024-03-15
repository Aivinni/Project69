import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.*;
import java.net.FileNameMap;

public class GamePanel extends JPanel implements Runnable {
    public static int tile_size = 48; // default tile size is 12
    public static final int MAX_SCREEN_COL = 31; // used for how to draw tiles
    public static final int MAX_SCREEN_ROW = 18;

    // GAME LOOP
    private Thread gameThread;
    // TRACKING INPUT
    KeyHandler keyH;
    // TEST VARIABLES
    private TaskForce[] sprites;
    public GamePanel() {
        TaskForce char1 = new TaskForce("wasd", new int[]{0, 0});
        TaskForce char2 = new TaskForce("arrows", new int[]{2, 0});
        sprites = new TaskForce[]{char1, char2};

        // setting up size of the panel
        this.setPreferredSize(new Dimension(tile_size * MAX_SCREEN_COL, tile_size * MAX_SCREEN_ROW));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);

        keyH = new KeyHandler();
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startGameThread();
        setUpWindow();
    }

    public static int[] getSizeMap() {
        return new int[]{MAX_SCREEN_ROW, MAX_SCREEN_COL};
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
            a.setPosition(a.getPosition()[0]-1, a.getPosition()[1]);
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

        ImageIcon bg = new ImageIcon("HMS Jervis badge.jpeg");
        Image bgImage = bg.getImage();
        g.drawImage(bgImage,sprites[0].getPosition()[1] * tile_size, sprites[0].getPosition()[0] * tile_size, tile_size, tile_size, null);

        g2D.setColor(Color.RED);
        g2D.setColor(Color.BLACK);
        g2D.fillRect(sprites[1].getPosition()[1] * tile_size, sprites[1].getPosition()[0] * tile_size, tile_size, tile_size);
    }

    private void setUpWindow() {
        JFrame window = new JFrame("2D GAME");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();
        window.setVisible(true);
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private BufferedImage loadSprite() {
        try {
            BufferedImage image = ImageIO.read(new File("resources/Naval_Ensign_of_the_United_Kingdom.svg.png"));
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}