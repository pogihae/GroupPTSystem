package view;

import model.Reservation;
import model.Trainer;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class UserView extends BaseView {
    private final Scanner sc = new Scanner(System.in);

    public void showLoginScreen() {
        println("☆.。.:*・°☆.。.:*・°☆.。.:*・°☆");
        println("           [ 로그인 ]           ");
        println("☆.。.:*・°☆.。.:*・°☆.。.:*・°☆");
    }

    /*
    ┊　　┊　　┊ 　 ┊    　┊　   ┊　 ┊
    ┊　　┊　　┊ 　 ☆    　┊　   ┊　 ┊
    ┊　　┊　　 ✬ 　 　   　✬ 　  ┊　 ┊
    ┊　　★ 　　　 　 　    　　　   ★　 ┊
    ☆ 　　 　　　 　 　    　　　　　　 ☆

     */
    public void showMainScreen() {
        String[] startLines = {
                "┊    ┊    ┊    ┊    ┊    ┊    ┊    ┊    ┊",
                "┊    ┊    ┊    ┊    ☆    ┊    ┊    ┊    ┊",
                "┊    ┊    ┊     ✬        ✬    ┊    ┊    ┊",
                "┊    ┊    ★      WELCOME      ★    ┊    ┊",
                "┊    ☆                             ☆    ┊",
                "✬            Group PT System            ✬",
                "                에 오신 것을               ",
                "                 환영합니다              "


        };
        for (String line : startLines) {
            println(line);
        }
    }

    public String requestUserMenus() throws IllegalAccessException {
        print("\n\n\n");
        print("                 MAIN MENU                   \n");
        return requestMenuSelect(
                "로그인",
                "회원가입",
                "상담예약",
                "상담확인"
        );
    }


    public void showSignupMenu() {
        println("☆.。.:*・°☆.。.:*・°☆.。.:*・°☆");
        println("        [ 회원 가입 ]");
        println("☆.。.:*・°☆.。.:*・°☆.。.:*・°☆");
    }

    public void printLoginFailed() {
        printlnError("로그인 실패");
    }

    public void printLoginSuccess(User user) {
        printSpecial("환영합니다. " + user.getName() + "님\n");
    }

    public String requestName() {
        print("이름: ");
        return readLine();
    }

    public int requestAge() {
        print("나이: ");
        return Integer.parseInt(readLine());

    }

    public String requestSex() {
        print("성별: ");
        return readLine();

    }

    public String requestId() {
        print("아이디: ");
        return readLine();

    }

    public String requestPw(String type) {
        if (type.equals("signUp")) println("비밀번호: 8자 이상의 영문 대/소문자, 숫자, 특수문자를 포함해 주세요.");
        print("비밀번호: ");
        return readLine();
    }

    public String requestPhoneNumber() {
        print("전화번호(010-****-****): ");
        return readLine();
    }

    public int requestRole() {
        print("번호를 선택하세요.");
        print("회원가입 유형 : (1)트레이너 (2)회원");
        return Integer.parseInt(sc.nextLine());

    }

    public void showSigned() {
        println(SEPARATOR);
        println("회원가입 신청이 완료되었습니다. 승인 메시지가 전송되면 로그인이 가능합니다.");
        println(SEPARATOR);

    }

    public String showConsultMenu() throws IllegalAccessException {
        print("\n\n\n");
        print("                Consulting                  \n");
        return requestMenuSelect("상담 예약", "상담 예약 확인");
    }

    public void showReserveConsultation() {
        println(SEPARATOR);
        ;
    }

    public String showTrainers(List<Trainer> trainers) {
        print(formatTitle("트레이너 목록"));
        //!!!!!!!!!!!!!!!!여기서 모든 트레이너의 이름과 전 월 수입이 전 월 수입이 높은순으로 출력!!!!!!!!!!!!!!!!
        for (int i = 0; i < trainers.size(); i++) {
            print((i + 1) + ": ");
            printUserAbstractInfo(trainers.get(i));
        }
        println(SEPARATOR);
        printRequestInput("확인할 트레이너의 번호");
        return readLine();
    }

    public void printUserAbstractInfo(User user) {
        println("[" + user.getName() + "] " + user.getSex() + " " + user.getAge() + "세 " + user.getId() + " " + user.getPhoneNumber());
    }

    /*

    (1)2024-04-02, 화 - 15:00 (4)2024-04-02, 화 - 18:00
    (2)2024-04-02, 화 - 16:00 (5)2024-04-02, 화 - 18:00
    (3)2024-04-02, 화 - 17:00 (6)2024-04-02, 화 - 18:00
    ####################
    */
    public String showAvailableTime(List<LocalDateTime> availableTime) {
        print(formatTitle("예약 가능 시간"));
        if (availableTime.isEmpty()) {
            printlnError("No available time slots.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, E - HH:mm");

        for (int i = 0; i < availableTime.size(); i++) {
            LocalDateTime dateTime = availableTime.get(i);
            println((i + 1) + ". " + dateTime.format(formatter));
        }

        println(SEPARATOR);
        printRequestInput("예약할 시간의 번호");
        return readLine();
    }

    public String showCheckMyReservation() {
        println(SEPARATOR);
        printRequestInput("예약하신 전화번호");
        return readLine();
    }

    public void printReservation(Reservation reservation) {
        print(formatTitle("예약 정보"));
        if (reservation == null) {
            printlnError("예약 없음");
        }
        printSpecial("담당자 : " + reservation.getManager().getName() + "\n");
        printSpecial("예약시간 : " + reservation.getStartDate() + "\n");
        printSpecial("예약자명 : " + reservation.getUsers().get(0).getName() + "님\n");
        printSpecial("전화번호 : " + reservation.getUsers().get(0).getPhoneNumber());
        println(SEPARATOR);
    }

    public String myReservationMenu() throws IllegalAccessException {
        print("\n\n\n");
        print("              My Reservation                \n");
        return requestMenuSelect("상담 변경", "상담 취소");
    }

    public void showResult(String type) {
        printSpecial(type + " 완료 되었습니다.\n");
    }

    public void printInvalid(int i) {
        switch (i) {
            case 1 -> printlnError("중복된 아이디 입니다.");
            case 2 -> printlnError("중복된 전화번호 입니다.");
            case 3 -> printlnError("유효하지 않은 아이디 입니다.");
            case 4 -> printlnError("유효하지 않은 비밀번호 입니다.");
            case 5 -> printlnError("유효하지 않은 전화번호 입니다.");
        }

    }
}
