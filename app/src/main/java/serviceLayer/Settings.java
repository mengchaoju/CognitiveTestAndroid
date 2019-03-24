package serviceLayer;

public class Settings {

    //Host name and port number of the remote server
    private String remoteHost = "10.12.13.214";
    private int remotePort = 5000;
    private int strokeWidth = 12;
    private int markPenWidth = 42;
    private int timeBetween2Trials = 20;  //180 seconds = 3 minutes
    //The pen colours
    private int startColour = 0xff0000;
    private int endColour = 0xff9900;
    private int[] colourRange = {0xff0000, 0xff9900, 0xffff00, 0x66ff00, 0x0000ff, 0x00ffff, 0x660066};

    public String getRemoteHost() {
        return this.remoteHost;
    }

    public int getRemotePort() {
        return this.remotePort;
    }

    public void setRemoteHost(String rmtHst) {
        this.remoteHost = rmtHst;
    }

    public void setRemotePort(int rmtPt) {
        this.remotePort = rmtPt;
    }

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

}
