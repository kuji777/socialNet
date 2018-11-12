package socialg.com.vyz.socialgaming.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vincent on 11/11/2018.
 */

public class Util {

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }
}
