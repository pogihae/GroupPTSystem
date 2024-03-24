package model;

import repository.GroupPTRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Member extends User{
    Scanner sc = new Scanner(System.in);
    int remainSessionCount;//남은수업횟수
    Payment payment;//결제 객체
    LocalDate paymentTime;
    Payment.PaymentOption selectedOption;

    public Member(User user) {//회원가입신청할 때 생성
        super(user.getName(), user.getPhoneNumber(), user.getAge(), user.getSex(), user.getId(), user.getPw(), user.getRole());
        this.remainSessionCount = 0; // 예시로 0으로 설정
        this.payment = null; // 초기값으로 null 설정
        this.paymentTime = null; // 현재 날짜로 초기화, 필요에 따라 다른 값을 사용할 수 있음
        this.selectedOption = null; // 초기값으로 null 설정
    }
    //수업결제
    public void payForClass(){
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
        this.remainSessionCount += selectedOption.getSessions();
        //payment 객체를 "결제" 파일에 저장하는 작업
        //노쇼 1회 시 재등록(결제) 불가능하다
    }
    //수업예약
    public void reserveClass(){
        GroupPTRepository repository = new GroupPTRepository();
        //남은회수 있는지 확인 후 => 있으면
        //1. 트레이너 선택메뉴 => 선택
        //2. 해당 트레이너의 수업 예약 가능한 시간 목록 출력 => 선택
        //3. 예약 확정 => 예약확정 메세지 출력
        if(selectedOption.getSessions()==0){
            System.out.println("남은 횟수가 없습니다. 재결제가 필요합니다");
            return;
        }
        //1. 트레이너 선택 메뉴(이름, 등급, 성별)
        List<Trainer> allTrainersList = repository.findAllTrainers();
        System.out.println("예약을 원하는 트레이너의 번호를 입력해주세요");
        for(int i=0; i<allTrainersList.size();i++) {
            Trainer trainer = allTrainersList.get(i);
            System.out.printf("%d. %s 트레이너/ 등급 %s/ %s\n", i+1, trainer.getName(), trainer.getGrade()
            , trainer.getSex());
        }
        System.out.print("번호 입력: ");
        int choice = sc.nextInt();

        //유효한 번호가 아니면 메뉴 다시 보여주기 구현해야함
        Trainer selectedTrainer = allTrainersList.get(choice-1);






        /*
        1. 원하는 시간이 없으면 ‘0’번을 눌러 다시 트레이너 목록이 보여진다.
        2. ‘*’을 누르면 메인화면으로 돌아간다.
        3. 선택지에 없는 메뉴를 선택할 경우 메뉴를 다시 출력한다.
         */
    }
    //수업정보확인
    //(수업 예약 확인, PT 선생님, 노쇼, 남은 수업 횟수,남은 수업 사용 가능 일수)
    public void displayReservationInfo(){

    }

    //수업예약취소
    //당일취소할 경우, 취소는 되지만 횟수가 차감된다

    //수업예약변경
    //당일예약변경 불가능하다
}
