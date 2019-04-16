package project.cognitivetest.presentationLayer.serviceLayer;

public class Settings {

    //Host name and port number of the remote server
    private String remoteHost = "192.168.1.3";
    private int remotePort = 5000;
    private int strokeWidth = 12;
    private int markPenWidth = 42;
    private int timeBetween2Trials = 20;  //180 seconds = 3 minutes
    //The pen colours
    private int startColour = 0xff0000;
    private int endColour = 0xff9900;
    private int[] colourRange = {0xff0000, 0xff9900, 0xffff00, 0x66ff00, 0x0000ff, 0x00ffff, 0x660066};
    /**
     * The following variables are used only for testing
     */
    private final String sampleJsonCommand = "{\"command\":\"test\",\"message\":\"it's a test message!\"}";
    private final String samplePixelData = "316.94336,276.92188,2019-03-30 19:32:27.474,1,0;322.81912,282.74768,2019-03-30 19:32:27.687,1,0;325.06232,285.5304,2019-03-30 19:32:27.71,1,0;339.507,304.89062,2019-03-30 19:32:27.953,1,0;339.9414,306.92188,2019-03-30 19:32:28.009,1,0;347.97363,319.96875,2019-03-30 19:32:28.062,1,0;348.9746,322.9375,2019-03-30 19:32:28.108,1,0;351.97754,324.89062,2019-03-30 19:32:28.158,1,0;353.99344,333.91394,2019-03-30 19:32:28.21,1,0;359.96094,341.92188,2019-03-30 19:32:28.258,1,0;362.98828,340.90625,2019-03-30 19:32:28.306,1,0;366.0355,338.875,2019-03-30 19:32:28.61,1,0;367.94434,338.875,2019-03-30 19:32:28.633,1,0;371.97266,326.92188,2019-03-30 19:32:28.676,1,0;377.97852,320.62854,2019-03-30 19:32:28.725,1,0;382.95898,311.48938,2019-03-30 19:32:28.774,1,0;386.89316,307.60803,2019-03-30 19:32:28.82,1,0;389.9414,302.90027,2019-03-30 19:32:28.873,1,0;396.03522,297.9375,2019-03-30 19:32:28.919,1,0;396.97266,292.9375,2019-03-30 19:32:28.967,1,0;398.9746,290.90625,2019-03-30 19:32:29.014,1,0;398.9746,288.875,2019-03-30 19:32:29.201,1,0;400.97656,286.92188,2019-03-30 19:32:29.244,1,0;";

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

    /**
     * The following functions are used only for testing
     */
    public String getSamplePixelData() {
        return this.samplePixelData;
    }
}
