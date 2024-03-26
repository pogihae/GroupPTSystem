package util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

public class Utils {

    @AllArgsConstructor
    @Getter
    public enum Day {
        SUN(1),MON(2), TUE(3), WED(4),
        THU(5), FRI(6), SAT(7);

        final int dayOfWeek;

        public static Day of(int dayOfWeek) {
            return Arrays.stream(Day.values())
                    .filter(d -> d.dayOfWeek == dayOfWeek)
                    .findFirst().orElseThrow();
        }

        public static Day of(String name) {
            return Arrays.stream(Day.values())
                    .filter(d -> d.name().equals(name))
                    .findFirst().orElseThrow();
        }
    }

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

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
    public static int getMonth(LocalDateTime date) {
        return date.getMonth().getValue();
    }

    public static Day getDay(LocalDateTime date) {
        return Day.of(date.getDayOfWeek().getValue());
    }

    public static int getDate(LocalDateTime date) {
        return date.getDayOfMonth();
    }

    public static int getYear(LocalDateTime date) {
        return date.getYear();
    }
}
