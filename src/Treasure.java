import java.awt.*;

public class Treasure extends Interactive{
    private Image hiddenImage;
    public Treasure(){
        super("edinburgh shipwreck", new int[]{8, 9}, "Ocean.png");
//        this.hiddenImage = hiddenImage;
    }

    public Image getHiddenImage() {
        return hiddenImage;
    }
}
