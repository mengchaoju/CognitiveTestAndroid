package until;

/**
 * Created by 50650 on 2019/4/5
 */
public class ServerIP {
    private static final String HOST = "10.0.2.2:5000";
    public static final String LOGINURL="http://"+HOST+"/stafflogin";
    public static final String SIGNUPURL="http://"+HOST+"/registerstaff";

    public static final String UPLOADCOPYPIXELS="http://"+HOST+"/copypixels";
    public static final String UPLOADRECALLPIXELS="http://"+HOST+"/recallpixels";
    public static final String UPLOADCOPYTIME="http://"+HOST+"/copytime";
    public static final String UPLOADRECALLTIME="http://"+HOST+"/recalltime";
}
