package project.cognitivetest.presentationLayer.until;

/**
 * Created by 50650 on 2019/4/5
 */
public class ServerIP {
    private static final String HOST = "10.0.2.2:5000";
    public static final String LOGINURL="http://"+HOST+"/stafflogin";
    public static final String SIGNUPURL="http://"+HOST+"/registerstaff";
    private final String TRIALSDATAURL="http://"+HOST+"/pixelsdata";

    public String getTRIALSDATAURL() {
        return this.TRIALSDATAURL;
    }
}
