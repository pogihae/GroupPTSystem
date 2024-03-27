package view;

import model.Reservation;
import service.UserService;
import util.Color;
import util.Utils;

import java.util.*;

public abstract class BaseView {
    private static final Scanner scanner = new Scanner(System.in);

    public void print(String line) {
        System.out.print(line);
    }

    public void println(String line) {
        System.out.println(line);
    }

    public String readLine() {
        return scanner.nextLine().trim();
    }

    public int readInt() {
        return Integer.parseInt(scanner.nextLine());
    }

    public boolean confirmAction(String message) {
        System.out.println(message + " (Y/N)");
        String input = readLine().toLowerCase();
        return input.equals("y");
    }

    public List<String> readLineBySeparate(String opt) {
        String line = readLine();
        return Arrays.stream(line.split(opt))
                .map(String::trim)
                .toList();
    }

    public String requestMenuSelect(String... menus) throws IllegalAccessException {
        System.out.print(formatMenu(menus));
        String input = readLine();

        if (input.equals("#")) {
            System.out.println("시스템종료");
            System.exit(0);
        }

        if (input.equals("0") && UserService.loginedUser != null) {
            System.out.println("로그아웃");
            UserService.logout();
            return "0";
        }

        int start = 1;
        int end = menus.length;
        int inputInt = Integer.parseInt(input);

        if (!(start <= inputInt && end >= inputInt)) {
            throw new IllegalAccessException("맞는 메뉴가 없습니다.");
        }

        return input;
    }

    public void showLogo(){
        String[] logoLines = {Color.CYAN+
                "      ██████╗ ██████╗  ██████╗ ██╗   ██╗██████╗     ██████╗ ████████╗    ███████╗██╗   ██╗███████╗████████╗███████╗███╗   ███╗",
                "██╔════╝ ██╔══██╗██╔═══██╗██║   ██║██╔══██╗    ██╔══██╗╚══██╔══╝    ██╔════╝╚██╗ ██╔╝██╔════╝╚══██╔══╝██╔════╝████╗ ████║",
                "██║  ███╗██████╔╝██║   ██║██║   ██║██████╔╝    ██████╔╝   ██║       ███████╗ ╚████╔╝ ███████╗   ██║   █████╗  ██╔████╔██║",
                "██║   ██║██╔══██╗██║   ██║██║   ██║██╔═══╝     ██╔═══╝    ██║       ╚════██║  ╚██╔╝  ╚════██║   ██║   ██╔══╝  ██║╚██╔╝██║",
                "╚██████╔╝██║  ██║╚██████╔╝╚██████╔╝██║         ██║        ██║       ███████║   ██║   ███████║   ██║   ███████╗██║ ╚═╝ ██║",
                "    ╚═════╝ ╚═╝  ╚═╝ ╚═════╝  ╚═════╝ ╚═╝         ╚═╝        ╚═╝       ╚══════╝   ╚═╝   ╚══════╝   ╚═╝   ╚══════╝╚═╝     ╚═╝"+ Color.ANSI_RESET
       };

        addSpace(logoLines);

    }

    public void showStart(){
        String[] startLines = {Color.CYAN+
                "              +-------------------------------------+",
                "|    해당 프로그램을 시작하시겠습니까?    |",
                "         |         [Y]es        [N]o         |",
                "              +-------------------------------------+"
        + Color.ANSI_RESET};
    addSpace(startLines);
    }
    //가운데 정렬을 위한 공백 추가 함수
    public void addSpace(String[] Lines){
        for (String line : Lines) {
            // 각 줄을 가운데 정렬하기 위해 앞에 공백 추가
            String centeredLine = centerLine(line);
            System.out.println(centeredLine);
        }
    }
    // 문자열의 앞에 붙일 공백의 길이를 계산
    public String centerLine(String line) {
        int consoleWidth = 174;
        int space = (consoleWidth - line.length()) / 2;
        return " ".repeat(Math.max(0, space)) + line;
    }

    public void printlnError(String msg) {
        println(Color.RED + msg + Color.ANSI_RESET);
    }

    private String formatMenu(String title, String... menus) {
        //변경가능한 문자열
        StringBuilder sb = new StringBuilder();
        sb.append("        .・*・.・*・.・*・.・*・.・*・.        \n");
        for (int i=0; i< menus.length; i++) {
            sb.append("\t\t\t\t%d. %s\t\t\t\t\t\n".formatted(i+1, menus[i]));
        }
        if (UserService.loginedUser != null) {
            sb.append("\t\t\t\t0. 로그아웃\t\t\t\t\t\n");
        }
        sb.append("\t\t\t\t#. 종료\t\t\t\t\t\t\n");
        sb.append("        ・.*・.・*・.・*・.・*・.・*.・        \n\n");
        sb.append("입력: ");
        return sb.toString();
    }

    public void printRequestInput(String target) {
        print("[%s]를 입력해주세요: ".formatted(target));
    }

    public String formatTitle(String title) {
        return "*********\t%s\t*********\n".formatted(title);
    }

    public void printSpecial(String msg) {
        println(Color.BLUE + msg + Color.ANSI_RESET);
    }

    public static String SEPARATOR = "*****************************";

    /**
     *
     * @param reservations 오늘부터 다음주 까지(ex. 오늘이 화요일이면 오늘 포함해서 다음 주 월요일까지)의 예약목록
     * */
    public void printReservations(List<Reservation> reservations) {
        System.out.println("******* 예약 목록 *******");
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
        println(SEPARATOR);
        StringBuilder sb = new StringBuilder();
        sb.append(formatTitle("예약목록"));
        for (Utils.Day day : sortedDays) {
            List<Reservation> dayReservations = dayToReservations.get(day);
            if (dayReservations.isEmpty()) continue;

            int date = Utils.getDate(dayReservations.get(0).getStartDate());
            sb.append("%s(%d)\n".formatted(day.name(), date));
            sb.append(SEPARATOR).append("\n");

            for (Reservation reservation : dayReservations) {
                sb.append(reservation.getStartDate()).append("\t");
                sb.append(reservation.getManager().getName()).append("\t");
                sb.append(reservation.getUsers().size()).append("명 예약\n");
            }
        }

        System.out.println(sb);
        println(SEPARATOR);
    }


}
