import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public static int tile_size = 24; // default tile size is 12
    public static final int MAX_SCREEN_COL = 164; // used for how to draw tiles
    public static final int MAX_SCREEN_ROW = 36;

    // GAME LOOP
    private Thread gameThread;
    // TRACKING INPUT
    KeyHandler keyH;
    // TEST VARIABLES
    int x = 0;
    int y = 0;
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
            if (delta >= 0.5) {
                // delta being 1 or greater means 1/60 of a second;
                repaint();
                if (keyH.isWKeyPressed()||keyH.isUpKeyPressed()) {
                    y--;
                }
                if (keyH.isSKeyPressed()||keyH.isDownKeyPressed()) {
                    y++;
                }
                if (keyH.isDKeyPressed()||keyH.isRightKeyPressed()) {
                    x++;
                }
                if (keyH.isAKeyPressed()||keyH.isLeftKeyPressed()) {
                    x--;
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
        g2D.fillRect(x, y, 24, 24);
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