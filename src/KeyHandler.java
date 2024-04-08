import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class KeyHandler implements KeyListener {
    private Game game;

    private String[] movementKey;
    private String interactKeyPressed = "";

    public KeyHandler(Game game){
        this.game = game;
        movementKey = new String[game.getSprites().size()];
        Arrays.fill(movementKey, "");
    }

    public String[] getMovementKey() {
        return movementKey;
    }
    public String getInteractKeyPressed() {
        return interactKeyPressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_W -> wKeyPressed = true;
//            case KeyEvent.VK_S -> sKeyPressed = true;
//            case KeyEvent.VK_A -> aKeyPressed = true;
//            case KeyEvent.VK_D -> dKeyPressed = true;
//            case KeyEvent.VK_UP -> upKeyPressed = true;
//            case KeyEvent.VK_DOWN -> downKeyPressed = true;
//            case KeyEvent.VK_LEFT -> leftKeyPressed = true;
//            case KeyEvent.VK_RIGHT -> rightKeyPressed = true;
//            case KeyEvent.VK_F -> interactKeyPressed = "f";
//            case KeyEvent.VK_SLASH -> interactKeyPressed = "/";
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_F -> interactKeyPressed = "f";
            case KeyEvent.VK_SLASH -> interactKeyPressed = "/";
        }

        if (movementKey.length > 0) {
            switch (code) {
                case KeyEvent.VK_W -> movementKey[0] = "w";
                case KeyEvent.VK_S -> movementKey[0] = "s";
                case KeyEvent.VK_A -> movementKey[0] = "a";
                case KeyEvent.VK_D -> movementKey[0] = "d";
            }
        }
        if (movementKey.length > 1) {
            switch (code) {
                case KeyEvent.VK_UP -> movementKey[1] = "up";
                case KeyEvent.VK_DOWN -> movementKey[1] = "down";
                case KeyEvent.VK_LEFT -> movementKey[1] = "left";
                case KeyEvent.VK_RIGHT -> movementKey[1] = "right";
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_F, KeyEvent.VK_SLASH -> interactKeyPressed = "";
        }

        if (movementKey.length > 0) {
            switch (code) {
                case KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D -> movementKey[0] = "";
            }
        }
        if (movementKey.length > 1) {
            switch (code) {
                case KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> movementKey[1] = "";
            }
        }
    }
}