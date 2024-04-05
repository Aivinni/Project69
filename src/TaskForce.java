public class TaskForce extends Interactive {
    private boolean sonarReady;
    private boolean usingSonar;

    private boolean activeSonarJustUsed;
    private boolean passiveSonarJustUsed;

    private double sonarScale;
    private double sonarScalePassive;
    private long lastSonarUseTime;
    private long lastPassivePulseTime;
    private boolean moveReady;

    public TaskForce(String name, int[] position, String fileName) {
        super(name, position, fileName);
        usingSonar = false;
        moveReady = true;
    }

    public void setSonarReady(boolean sonarReady) {
        this.sonarReady = sonarReady;
    }
    public boolean isSonarReady() {
        return sonarReady;
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
        sonarScalePassive += (0.2 - Math.sqrt(sonarScalePassive / 100));
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
