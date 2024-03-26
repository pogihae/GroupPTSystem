package view;

import model.Reservation;
import model.Trainer;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private final Scanner sc = new Scanner(System.in);
    public void showSignupMenu() {
        System.out.println("*************************************************");
        System.out.println("형식에 맞는 양식을 작성해 주세요.");
        System.out.println("*************************************************");
    }

    public void printLoginFailed() {
        System.out.println("로그인 실패");
    }

    public void printLoginSuccess(User user) {
        System.out.println("로그인 성공");
        System.out.println(user);
    }

    public String requestName() {
        System.out.print("이름: ");
        return sc.nextLine();
    }

    public int requestAge() {
        System.out.print("나이: ");
        return Integer.parseInt(sc.nextLine());

    }

    public String requestSex() {
        System.out.print("성별: ");
        return sc.nextLine();

    }

    public String requestId() {
        System.out.print("아이디: ");
        return sc.nextLine();

    }

    public String requestPw() {
        System.out.print("비밀번호: ");
        return sc.nextLine();

    }

    public String requestPhoneNumber() {
        System.out.print("휴대폰번호: ");
        return sc.nextLine();
    }

    public int requestRole() {
        System.out.print("번호를 선택하세요.");
        System.out.print("회원가입 유형 : (1)트레이너 (2)회원");
        return Integer.parseInt(sc.nextLine());

    }

    public void showSigned() {
        System.out.println("*************************************************");
        System.out.println("회원가입 신청이 완료되었습니다. 승인 메시지가 전송되면 로그인이 가능합니다.");
        System.out.println("*************************************************");

    }

    public String showConsultMenu() {
        System.out.println("*************************************************");
        System.out.println("메뉴를 선택하세요.");
        System.out.println("1.상담 예약, 2.상담 예약 확인");
        return sc.nextLine();
    }

    public void showReserveConsultation() {
        System.out.println("*************************************************");
    }

    public String showTrainers(List<Trainer> trainers){
        Trainer trainer;
        for(int i = 0;i<trainers.size();i++){
            trainer = trainers.get(i);
            System.out.printf("%d. %s 트레이너/ 등급 %s/ %s\n", i+1, trainer.getName(), trainer.getGrade(), trainer.getSex());
        }
        System.out.println("*************************************************");
        System.out.println("원하시는 트레이너의 번호를 입력하세요.:");
        return sc.nextLine();
    }

    /*

    (1)2024-04-02, 화 - 15:00 (4)2024-04-02, 화 - 18:00
    (2)2024-04-02, 화 - 16:00 (5)2024-04-02, 화 - 18:00
    (3)2024-04-02, 화 - 17:00 (6)2024-04-02, 화 - 18:00
    ####################
    */
    public String showAvailableTime(List<LocalDateTime> availableTime){
        if (availableTime.isEmpty()) {
            System.out.println("No available time slots.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, E - HH:mm");

        for (int i = 0; i < availableTime.size(); i++) {
            LocalDateTime dateTime = availableTime.get(i);
            System.out.println((i+1) + ". " + dateTime.format(formatter));
        }

        System.out.println("--------------------------------------------");
        return sc.nextLine();
    }

    public String showCheckMyReservation(){
        System.out.println("*************************************************");
        System.out.println("예약하신 전화번호를 입력해주세요: ");
        return  sc.nextLine();
    }
    public void printReservation(Reservation reservation){
        System.out.println("*************************************************");
        System.out.println("담당자 : "+reservation.getManager().getName());
        System.out.println("예약자명 : "+reservation.getUsers().get(0).getName()+"님");
        System.out.println("전화번호 : "+reservation.getUsers().get(0).getPhoneNumber());
    }

    public String myReservationMenu() {
        System.out.println("*************************************************");
        System.out.println("메뉴를 선택하세요.");
        System.out.println("1.상담 변경, 2.상담 취소");
        return sc.nextLine();
    }

    public void showResult(String type){
        System.out.println(type+" 완료 되었습니다.");
    }

    public void printRegisterFailed(int i) {
        switch (i){
            case 1 -> System.out.println("중복된 아이디 입니다.");
            case 2 -> System.out.println("중복된 전화번호 입니다.");
            case 3 -> System.out.println("유효하지 않은 아이디 입니다.");
            case 4 -> System.out.println("유효하지 않은 비밀번호 입니다.");
            case 5 -> System.out.println("유효하지 않은 전화번호 입니다.");
        }

    }
}
