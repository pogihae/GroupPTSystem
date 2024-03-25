package view;

import model.Reservation;
import util.Utils;

import java.util.*;

public abstract class BaseView {
    private static final Scanner scanner = new Scanner(System.in);

    public String readLine() {
        return scanner.nextLine().trim();
    }

    public void requestMenuSelect(String... menus) {
        System.out.println(formatMenu(menus));
    }

    private String formatMenu(String... menus) {
        StringBuilder sb = new StringBuilder();
        sb.append("*******************\n");
        for (int i=0; i< menus.length; i++) {
            sb.append("%d. %s\n".formatted(i+1, menus[i]));
        }
        sb.append("0. 이전 메뉴\n");
        sb.append("*. 메인 메뉴\n");
        sb.append("*******************\n");
        sb.append("입력: ");
        return sb.toString();
    }

    /**
     *
     * @param reservations 오늘부터 다음주 까지(ex. 오늘이 화요일이면 오늘 포함해서 다음 주 월요일까지)의 예약목록
     * */
    public void printReservations(List<Reservation> reservations) {
        // 시간순 정렬
        reservations.sort(Comparator.comparing(Reservation::getStartDate));

        // 요일별 매핑
        Map<Utils.Day, List<Reservation>> dayToReservations = new HashMap<>();
        for (Utils.Day day : Utils.Day.values()) {
            dayToReservations.put(day, new ArrayList<>());
        }

        for (Reservation reservation : reservations) {
            Utils.Day day = Utils.getDay(reservation.getStartDate());
            dayToReservations.get(day).add(reservation);
        }

        // 날짜순 요일 정렬
        List<Utils.Day> sortedDays = reservations.stream()
                .map(r -> Utils.getDay(r.getStartDate()))
                .distinct()
                .toList();

        // 출력
        StringBuilder sb = new StringBuilder();
        for (Utils.Day day : sortedDays) {
            List<Reservation> dayReservations = dayToReservations.get(day);
            if (dayReservations.isEmpty()) continue;

            int date = Utils.getDate(dayReservations.get(0).getStartDate());
            sb.append("*********%s(%d)*********\n".formatted(day.name(), date));

            for (Reservation reservation : dayReservations) {
                sb.append(reservation.toString()).append('\n');
            }
        }
        sb.append("************************");

        System.out.println(sb);
    }
}
