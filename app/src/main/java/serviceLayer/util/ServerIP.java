package serviceLayer.util;

/**
 * Created by 50650 on 2019/4/5
 */
public class ServerIP {
    private static final String HOST = "10.0.2.2:5000";
    public static final String LOGINURL="http://"+HOST+"/stafflogin";
    public static final String SIGNUPURL="http://"+HOST+"/registerstaff";
    public static final String QUERYPARTICIPANT="http://"+HOST+"/queryParticipant";
    public static final String PARTICIPANTSIGNUP="http://"+HOST+"/registerParticipant";
    public static final String TRIALSDATAURL="http://"+HOST+"/pixelsdata";
    public static final String UPLOADCOPY="http://"+HOST+"/uploadcopy";
    public static final String UPLOADRECALL="http://"+HOST+"/uploadrecall";
}
