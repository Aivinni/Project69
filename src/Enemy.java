public class Enemy extends Detectable {
    public Enemy(String name, int[] position){
        super(name, position, "Ocean.png", "154.png");
    }
    public void move(){
        int[] position = getPosition();
        setPosition(position[1], position[0]);
        //todo: enemies move randomly every second in random directions
    }
}
