public class Enemy extends Detectable {
    int[] positionTarget;
    boolean targetReached;

    public Enemy(String name, int[] position, Game game){
        super(name, position, "Ocean.png", "154.png", game);
        targetReached = true;
    }

    public void setPositionTarget(int[] positionTarget) {
        this.positionTarget = positionTarget;
        targetReached = false;
    }
    public boolean isTargetReached() {
        return targetReached;
    }

    public void move(){
        int[] position = getPosition();

        if (isDetected()) {
            TaskForce closest = getGame().getClosest(this);
            if (Math.random() > 0.5) {
                if (position[0] < closest.getPosition()[0]) {
                    setPosition(position[0] - 1, position[1]);
                } else if (position[0] > closest.getPosition()[0]) {
                    setPosition(position[0] + 1, position[1]);
                } else {
                    if (position[1] < closest.getPosition()[1]) {
                        setPosition(position[0], position[1] - 1);
                    } else if (position[1] > closest.getPosition()[1]) {
                        setPosition(position[0] , position[1] + 1);
                    }
                }
            } else {
                if (position[1] < closest.getPosition()[1]) {
                    setPosition(position[0], position[1] - 1);
                } else if (position[1] > closest.getPosition()[1]) {
                    setPosition(position[0] , position[1] + 1);
                } else {
                    if (position[0] < closest.getPosition()[0]) {
                        setPosition(position[0] - 1, position[1]);
                    } else if (position[0] > closest.getPosition()[0]) {
                        setPosition(position[0] + 1, position[1]);
                    }
               }
            }
        } else {
            if (!targetReached) {
                if (positionTarget != null) {
                    if (Math.random() > 0.5) {
                        if (positionTarget[0] > position[0]) {
                            setPosition(position[0] + 1, position[1]);
                        } else if (positionTarget[0] < position[0]) {
                            setPosition(position[0] - 1, position[1]);
                        } else {
                            if (positionTarget[1] > position[1]) {
                                setPosition(position[0], position[1] + 1);
                            } else if (positionTarget[1] < position[1]) {
                                setPosition(position[0], position[1] - 1);
                            } else {
                                targetReached = true;
                            }
                        }
                    } else {
                        if (positionTarget[1] > position[1]) {
                            setPosition(position[0], position[1] + 1);
                        } else if (positionTarget[1] < position[1]) {
                            setPosition(position[0], position[1] - 1);
                        } else {
                            if (positionTarget[0] > position[0]) {
                                setPosition(position[0] + 1, position[1]);
                            } else if (positionTarget[0] < position[0]) {
                                setPosition(position[0] - 1, position[1]);
                            } else {
                                targetReached = true;
                            }
                        }
                    }
                } else {
                    double movementChance = Math.random();
                    double moveUpChance = 0.75;
                    double moveDownChance = 0.50;
                    double moveRightChance = 0.25;
                    double moveLeftChance = 0.0;
                    if (movementChance > moveUpChance) {
                        setPosition(position[0] - 1, position[1]);
                    } else if (movementChance > moveDownChance) {
                        setPosition(position[0] + 1, position[1]);
                    } else if (movementChance > moveRightChance) {
                        setPosition(position[0], position[1] + 1);
                    } else if (movementChance > moveLeftChance) {
                        setPosition(position[0], position[1] - 1);
                    }
                }
            } else {
                double movementChance = Math.random();
                double moveUpChance = 0.75;
                double moveDownChance = 0.50;
                double moveRightChance = 0.25;
                double moveLeftChance = 0.0;
                if (movementChance > moveUpChance) {
                    setPosition(position[0] - 1, position[1]);
                } else if (movementChance > moveDownChance) {
                    setPosition(position[0] + 1, position[1]);
                } else if (movementChance > moveRightChance) {
                    setPosition(position[0], position[1] + 1);
                } else if (movementChance > moveLeftChance) {
                    setPosition(position[0], position[1] - 1);
                }
            }
        }
    }
}
