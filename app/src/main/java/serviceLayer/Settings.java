package serviceLayer;

public class Settings {

    //Host name and port number of the remote server
    private String remoteHost = "10.12.13.214";
    private int remotePort = 5000;
    private int strokeWidth = 12;
    private int timeBetween2Trials = 180;  //180 seconds = 3 minutes

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

    public int getTimeBetween2Trials() {
        return this.timeBetween2Trials;
    }
}
