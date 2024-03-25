package view;

import model.Reservation;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class UserView {
    void displayBasicMenuForUser(){
        System.out.println("0. 유저 메뉴로 돌아가기");
        System.out.println("*. 로그인 화면으로 돌아가기");
        System.out.println("************************************************");
        System.out.print("원하는 메뉴를 선택하세요.");

    }

    public void showSignupMenu() {
        System.out.println("*************************************************");
        System.out.println("형식에 맞는 양식을 작성해 주세요.");
        System.out.println("*************************************************");
    }

    public void requestName() {
        System.out.print("이름: ");
    }

    public void requestAge() {
        System.out.print("나이: ");
    }

    public void requestSex() {
        System.out.print("성별: ");
    }

    public void requestId() {
        System.out.print("아이디: ");
    }

    public void requestPw() {
        System.out.print("비밀번호: ");
    }

    public void requestPhoneNumber() {
        System.out.print("휴대폰번호: ");
    }

    public void requestRole() {
        System.out.print("뭘로가입? : ");
    }

    public void showSigned() {
        System.out.println("*************************************************");
        System.out.println("회원가입 신청이 완료되었습니다. 승인 메시지가 전송되면 로그인이 가능합니다.");
        System.out.println("*************************************************");

    }

    public void showConsultMenu() {
        System.out.println("*************************************************");
        System.out.println("메뉴를 선택하세요.");
        System.out.println("1.상담 예약, 2.상담 예약 확인");
    }

    public void showReserveConsultation() {
        System.out.println("*************************************************");
    }

    public void showListofTrainers(int i,String name, String sex, String grade){
        System.out.printf("%d. %s 트레이너/ 등급 %s/ %s\n", i+1, name, grade, sex);
    }
    public void trainersListMenu() {
        System.out.println("*************************************************");
        System.out.println("원하시는 트레이너의 번호를 입력하세요.:");
    }

    /*
    ####################
    (1)1~2 (4)4~5 (7)7~8
    (2)2~3 (5)5~6
    (3)3~4 (6)6~7
    ####################
    */
    public void showAvailableTime(List<LocalDateTime> availableTime){
        if (availableTime.isEmpty()) {
            System.out.println("No available time slots.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, E - HH:mm");

        for (int i = 0; i < availableTime.size(); i++) {
            LocalDateTime dateTime = availableTime.get(i);
            System.out.println((i+1) + ". " + dateTime.format(formatter));
        }

        System.out.println("--------------------------------------------");
    }




    public void showCheckMyReservation(){
        System.out.println("*************************************************");
        System.out.println("예약하신 전화번호를 입력해주세요: ");
    }
    public void printReservation(Reservation reservation){
        System.out.println(reservation);
    }

    public void showMyReservation(User user) {

    }

    public void showChangeReservation(){

    }

    public void showCancelReservation(){

    }


}
