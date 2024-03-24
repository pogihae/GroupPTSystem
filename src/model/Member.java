package model;

import java.time.LocalDate;
import java.util.Scanner;

public class Member extends User{
    int remainSessionCount;//남은수업횟수
    Payment payment;//결제 객체
    LocalDate paymentTime;
    Payment.PaymentOption selectedOption;

    public Member(User user) {//회원가입신청할 때 생성
        super(user.getName(), user.getPhoneNumber(), user.getAge(), user.getSex(), user.getId(), user.getPw());
        this.remainSessionCount = 0; // 예시로 0으로 설정
        this.payment = null; // 초기값으로 null 설정
        this.paymentTime = null; // 현재 날짜로 초기화, 필요에 따라 다른 값을 사용할 수 있음
        this.selectedOption = null; // 초기값으로 null 설정
    }
    //수업결제
    public void payForClass(){
        Scanner sc = new Scanner(System.in);
        System.out.println("결제 단위를 선택해주세요");
        System.out.println("1. 10회 - 90일 - 총가격 ₩700,000");
        System.out.println("2. 20회 - 120일 - 총가격 ₩1,200,000");
        System.out.println("3. 30회 - 180일 - 회당 ₩1,500,000");
        System.out.print("번호 입력: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                selectedOption = Payment.PaymentOption.OPTION_1;
                break;
            case 2:
                selectedOption = Payment.PaymentOption.OPTION_2;
                break;
            case 3:
                selectedOption = Payment.PaymentOption.OPTION_3;
                break;
            default:
                System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                return; // 메소드 종료
        }
        this.payment = new Payment(LocalDate.now(), this.getPhoneNumber(),this.selectedOption);
        System.out.println(choice+"번 옵션, "+selectedOption.getPrice()+"원 결제가 완료되었습니다");

        //payment 객체를 "결제" 파일에 저장하는 작업
    }
    //수업예약

    //수업정보확인
    public void displayReservationInfo(){

    }
}
