package serviceLayer;

public class Settings {

    private int strokeWidth = 12;
    private int markPenWidth = 32;
    private int timeBetween2Trials = 20;  //180 seconds = 3 minutes
    //The pen colours
    private int startColour = 0xff0000;
    private int endColour = 0xff9900;
    private int[] colourRange = {0xff0000, 0xff9900, 0xffff00, 0x66ff00, 0x0000ff, 0x00ffff, 0x660066};
    private int enableColour = 0;  // 0 means disable, 1 means enable.
    private int retryTime = 500;  // retryTime*20 = The milliseconds of time before retrying when sending trials data

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public int getMarkPenWidth() {
        return this.markPenWidth;
    }
    public int getTimeBetween2Trials() {
        return this.timeBetween2Trials;
    }

    public int getStartColour() {
        return this.startColour;
    }

    public int getEndColour() {
        return this.endColour;
    }

    public int[] getColourRange() {
        return this.colourRange;
    }

    public int getEnableColour() {
        return this.enableColour;
    }

    public int getRetryTime() {
        return this.retryTime;
    }

}
