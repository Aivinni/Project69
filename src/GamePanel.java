import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public static int tile_size = 12; // default tile size is 12
    public static final int MAX_SCREEN_COL = 128; // used for how to draw tiles
    public static final int MAX_SCREEN_ROW = 72;

    // GAME LOOP
    private Thread gameThread;
    // TRACKING INPUT
    KeyHandler keyH;
    // TEST VARIABLES
    int x = 20;
    int x2 = 100;
    int y2 = 100;
    int y = 20;
    public GamePanel() {
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

            // the time between now and the least time this looped
            delta += (double) (currentTime - previousTime) / drawInterval;

            previousTime = currentTime;
                // delta being 1 or greater means 1/60 of a second;
            if (delta >= 1.2) {
                repaint();
                if (keyH.isWKeyPressed()) {
                    y--;
                }
                if (keyH.isUpKeyPressed()){
                    y2--;
                }
                if (keyH.isSKeyPressed()) {
                    y++;
                }
                if (keyH.isDownKeyPressed()){
                    y2++;
                }
                if (keyH.isDKeyPressed()) {
                    x++;
                }
                if (keyH.isRightKeyPressed()){
                    x2++;
                }
                if (keyH.isAKeyPressed()) {
                    x--;
                }
                if (keyH.isLeftKeyPressed()){
                    x2--;
                }
                delta = 0;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.PINK);
        g2D.fillRect(x, y, 12, 12);
        g2D.setColor(Color.BLACK);
        g2D.fillRect(x2, y2, 12, 12);
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