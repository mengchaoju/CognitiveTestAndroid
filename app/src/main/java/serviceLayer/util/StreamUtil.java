package serviceLayer.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 50650 on 2019/4/16
 */
public class StreamUtil {

    public static String StramToString(InputStream is) {
        // During the process of reading, the contents read are stored in the value cache,
        // and then converted into a string and returned at one time
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // Read stream operation, read to no end (loop)
        byte[] buffer = new byte[1024];
        // A temporary variable that records what was read
        int temp = -1;
        // Each read 1024 bytes, if can read, return assigned to temp, as long as it is not -1,
        // that there is content
        try {
            while ((temp = is.read(buffer)) != -1) {
                bos.write(buffer, 0, temp);

            }
            //Return read data
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }}