import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public static int tile_size = 12; // default tile size is 12
    public static final int MAX_SCREEN_COL = 110; // used for how to draw tiles
    public static final int MAX_SCREEN_ROW = 70;

    // GAME LOOP
    private Thread gameThread;
    // TRACKING INPUT
    KeyHandler keyH;
    // TEST VARIABLES
    private TaskForce[] sprites;
//    int x = 20;
//    int x2 = 100;
//    int y2 = 100;
//    int y = 20;
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
            if (delta >= 1.2) {
                repaint();
                if (keyH.isWKeyPressed()) {
                    move("w", 0);
//                    y--;
                }
                if (keyH.isSKeyPressed()) {
                    move("S", 0);
//                    y++;
                }
                if (keyH.isDKeyPressed()) {
                    move("D", 0);
//                    x++;
                }
                if (keyH.isAKeyPressed()) {
                    move("A", 0);
//                    x--;
                }

                if (keyH.isUpKeyPressed()){
                    move("w", 1);
//                    y2--;
                }
                if (keyH.isDownKeyPressed()){
                    move("S", 1);
//                    y2++;
                }
                if (keyH.isRightKeyPressed()){
                    move("D", 1);
//                    x2++;
                }
                if (keyH.isLeftKeyPressed()){
                    move("A", 1);
//                    x2--;
                }
                delta = 0;
            }
        }
    }
    private void move(String direction, int sprite){
        TaskForce a = sprites[sprite];
        if (direction.toUpperCase().equals("W")&& a.getPosition()[0] > 0) {
            a.setPosition(a.getPosition()[0]-1, a.getPosition()[1]);
        }
        if (direction.toUpperCase().equals("A")&& a.getPosition()[1] > 0) {
            a.setPosition(a.getPosition()[0], a.getPosition()[1] - 1);
        }
        if (direction.toUpperCase().equals("S")&& a.getPosition()[0] < MAX_SCREEN_ROW - 1) {
            a.setPosition(a.getPosition()[0] + 1, a.getPosition()[1]);
        }
        if (direction.toUpperCase().equals("D")&& a.getPosition()[1] < MAX_SCREEN_COL - 1) {
            a.setPosition(a.getPosition()[0], a.getPosition()[1] + 1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;


        g2D.setColor(Color.PINK);
        g2D.fillRect(sprites[0].getPosition()[1] * tile_size, sprites[0].getPosition()[0] * tile_size, 12, 12);

        g2D.setColor(Color.BLACK);
        g2D.fillRect(sprites[1].getPosition()[1] * tile_size, sprites[1].getPosition()[0] * tile_size, 12, 12);
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
}