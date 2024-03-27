package view;

import model.Reservation;
import model.Trainer;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class UserView extends BaseView{
    private final Scanner sc = new Scanner(System.in);
    public void printLoginScreen(){
        //로그인 화면 ID, PW 입력하세요
    }
    public String requestUserMenus() throws IllegalAccessException {
        // 첫 화면 출력
        showLogo();
        return requestMenuSelect(
                "메인 메뉴",
                "로그인",
                "회원가입",
                "상담예약",
                "상담확인"
        );
    }
    public void showSignupMenu() {
        println("****************************");
        println("        [ 회원 가입 ]");
        println("****************************");
    }

    public void printLoginFailed() {
        println("로그인 실패");
    }

    public void printLoginSuccess(User user) {
        println("로그인 성공");
        System.out.println(user);
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
        if(type.equals("signUp")) println("비밀번호: 8자 이상의 영문 대/소문자, 숫자, 특수문자를 포함해 주세요.");
        print("비밀번호: ");
        return readLine();
    }

    public String requestPhoneNumber(String type) {
        println("전화번호: 010-****-**** 형식으로 작성해주세요.");
        print("휴대폰번호: ");
        return readLine();
    }

    public int requestRole() {
        print("번호를 선택하세요.");
        print("회원가입 유형 : (1)트레이너 (2)회원");
        return Integer.parseInt(sc.nextLine());

    }

    public void showSigned() {
        println("*************************************************");
        println("회원가입 신청이 완료되었습니다. 승인 메시지가 전송되면 로그인이 가능합니다.");
        println("*************************************************");

    }

    public String showConsultMenu() throws IllegalAccessException {
        return requestMenuSelect("상담 예약","상담 예약 확인");
    }

    public void showReserveConsultation() {
        println("****************************");
    }

    public String showTrainers(List<Trainer> trainers){
        Trainer trainer;
        for(int i = 0;i<trainers.size();i++){
            trainer = trainers.get(i);
            System.out.printf("%d. %s 트레이너/ 등급 %s/ %s\n", i+1, trainer.getName(), trainer.getGrade(), trainer.getSex());
        }
        println("****************************");
        println("원하시는 트레이너의 번호를 입력하세요.:");
        return readLine();
    }

    /*

    (1)2024-04-02, 화 - 15:00 (4)2024-04-02, 화 - 18:00
    (2)2024-04-02, 화 - 16:00 (5)2024-04-02, 화 - 18:00
    (3)2024-04-02, 화 - 17:00 (6)2024-04-02, 화 - 18:00
    ####################
    */
    public String showAvailableTime(List<LocalDateTime> availableTime){
        if (availableTime.isEmpty()) {
            println("No available time slots.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, E - HH:mm");

        for (int i = 0; i < availableTime.size(); i++) {
            LocalDateTime dateTime = availableTime.get(i);
            println((i+1) + ". " + dateTime.format(formatter));
        }

        println("--------------------------------------------");
        return readLine();
    }

    public String showCheckMyReservation(){
        println("****************************");
        println("예약하신 전화번호를 입력해주세요: ");
        return  readLine();
    }
    public void printReservation(Reservation reservation){
        if (reservation == null) {
            println("예약 없음");
        }
        println("담당자 : "+reservation.getManager().getName());
        println("예약자명 : "+reservation.getUsers().get(0).getName()+"님");
        println("전화번호 : "+reservation.getUsers().get(0).getPhoneNumber());
    }

    public String myReservationMenu() throws IllegalAccessException {
        return requestMenuSelect("상담 변경", "상담 취소");
    }

    public void showResult(String type){
        println(type+" 완료 되었습니다.");
    }

    public void printInvalid(int i) {
        switch (i){
            case 1 -> println("중복된 아이디 입니다.");
            case 2 -> println("중복된 전화번호 입니다.");
            case 3 -> println("유효하지 않은 아이디 입니다.");
            case 4 -> println("유효하지 않은 비밀번호 입니다.");
            case 5 -> println("유효하지 않은 전화번호 입니다.");
        }

    }
}
