public class TaskForce extends Interactive {
    private boolean usingSonar;

    private boolean activeSonarJustUsed, passiveSonarJustUsed;

    private double sonarScale, sonarScalePassive;

    private long lastSonarUseTime, lastPassivePulseTime;

    private boolean moveReady;
    private final String sonarKey, upKey, downKey, leftKey, rightKey;

    private double moveTime = 0.0;
    private boolean enemyNear = false;

    public TaskForce(String name, int[] position, Game game, String fileName, String sonarKey, String upKey, String downKey, String leftKey, String rightKey) {
        super(name, position, fileName, game);
        usingSonar = false;
        moveReady = true;
        this.sonarKey = sonarKey;
        this.upKey = upKey;
        this.downKey = downKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
    }

    public String getSonarKey() {
        return sonarKey;
    }

    public String getUpKey() {
        return upKey;
    }
    public String getDownKey() {
        return downKey;
    }
    public String getLeftKey() {
        return leftKey;
    }
    public String getRightKey() {
        return rightKey;
    }

    public void setMoveTime(double moveTime) {
        this.moveTime = moveTime;
    }
    public double getMoveTime() {
        return moveTime;
    }

    public void setEnemyNear(boolean enemyNear) {
        this.enemyNear = enemyNear;
    }
    public boolean isEnemyNear() {
        return enemyNear;
    }

    public boolean isUsingSonar(){
        return usingSonar;
    }
    public void toggleSonarOn(){
        usingSonar = true;
    }
    public double getSonarScale() {
        return sonarScale;
    }
    public double getPassiveSonarScale(){
        return sonarScalePassive;
    }

    public void setActiveSonarJustUsed(boolean activeSonarJustUsed) {
        this.activeSonarJustUsed = activeSonarJustUsed;
    }
    public boolean isActiveSonarJustUsed() {
        return activeSonarJustUsed;
    }

    public void setPassiveSonarJustUsed(boolean passiveSonarJustUsed) {
        this.passiveSonarJustUsed = passiveSonarJustUsed;
    }
    public boolean isPassiveSonarJustUsed() {
        return passiveSonarJustUsed;
    }

    public boolean isMoveReady() {
        return moveReady;
    }
    public void setMoveReady(boolean moveReady) {
        this.moveReady = moveReady;
    }

    public void incrementSonarScale() {
        sonarScale += (0.3 - Math.sqrt(sonarScale / 75));
        if (sonarScale >= 5.0) {
            usingSonar = false;
            activeSonarJustUsed = true;
            sonarScale = 0;
        }
    }
    public void incrementPassiveSonarScale(){
        sonarScalePassive += (0.2 - Math.sqrt(sonarScalePassive / 120));
        if (sonarScalePassive >= 3.0) {
            sonarScalePassive = 0;
            passiveSonarJustUsed = true;
        }
    }
    public void resetPassiveSonarScale() {
        sonarScalePassive = 0;
    }
    public void setLastSonarUseTime(){
        lastSonarUseTime = System.nanoTime();
    }
    public long getLastSonarUseTime(){
        return lastSonarUseTime;
    }
    public void setLastPassivePulseTime(){
        lastPassivePulseTime = System.nanoTime();
    }
    public long getLastPassivePulseTime(){
        return lastPassivePulseTime;
    }

}
