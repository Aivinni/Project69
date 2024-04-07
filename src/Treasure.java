import java.awt.*;

public class Treasure extends Detectable {
    public Treasure(int[] position, Game game){
        super("treasure", position, "Ocean.png", "Treasure.png", game);
    }
}
