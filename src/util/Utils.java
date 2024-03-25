package util;

import java.util.Date;

public class Utils {

    private Utils() {}

    public static String makeMenuString(String... menus) {
        StringBuilder sb = new StringBuilder();
        sb.append("***********************************\n");
        for (String menu : menus) {
            sb.append(menu).append('\n');
        }
        sb.append("***********************************\n");
        return sb.toString();
    }

    /**
     * 현재 시각과 비교
     * @return 현재 시각보다 과거: true, 현재 시각보다 미래: false
     * */
    public static boolean isOverDate(Date date) {
        Date cur = new Date();
        return cur.before(date);
    }
}
